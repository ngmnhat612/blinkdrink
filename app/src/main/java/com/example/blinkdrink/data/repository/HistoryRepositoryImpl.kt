// ============================================================================
// FILE: data/repository/HistoryRepositoryImpl.kt
// ============================================================================
package com.example.blinkdrink.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.blinkdrink.data.model.entity.WaterLogEntity
import com.example.blinkdrink.data.model.entity.EyeBreakLogEntity
import com.example.blinkdrink.data.source.local.HistoryLocalDataSource
import com.example.blinkdrink.data.source.local.database.dao.WaterLogDao
import com.example.blinkdrink.data.source.local.database.dao.EyeBreakLogDao
import com.example.blinkdrink.domain.model.DayProgress
import com.example.blinkdrink.domain.model.HistoryRecord
import com.example.blinkdrink.domain.model.WaterStatistics
import com.example.blinkdrink.domain.repository.HistoryRepository
import com.example.blinkdrink.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class HistoryRepositoryImpl @Inject constructor(
    private val localDataSource: HistoryLocalDataSource,
    private val waterLogDao: WaterLogDao,
    private val eyeBreakLogDao: EyeBreakLogDao,
    private val settingsRepository: SettingsRepository
) : HistoryRepository {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // ========================================================================
    // TODAY HISTORY (GIỮ NGUYÊN - Dùng DataStore cho UI nhanh)
    // ========================================================================
    override fun getTodayHistory(): Flow<List<HistoryRecord>> {
        return localDataSource.getHistoryRecordsJson().map { json ->
            if (json.isEmpty()) {
                emptyList()
            } else {
                parseHistoryRecords(json)
            }
        }
    }

    override suspend fun addHistoryRecord(record: HistoryRecord) {
        // Lưu vào DataStore (để hiển thị nhanh)
        val currentJson = localDataSource.getHistoryRecordsJson().first()
        val currentList = if (currentJson.isEmpty()) {
            emptyList()
        } else {
            parseHistoryRecords(currentJson)
        }
        val newList = listOf(record) + currentList
        val newJson = serializeHistoryRecords(newList)
        localDataSource.setHistoryRecordsJson(newJson)

        // ✅ Lưu vào Room Database (để tính toán thống kê)
        if (record.type == "Uống nước") {
            val amount = record.amount.replace(" ml", "").toIntOrNull() ?: 0
            val today = LocalDate.now().format(dateFormatter)

            waterLogDao.insert(
                WaterLogEntity(
                    date = today,
                    amountMl = amount,
                    timestamp = record.timestamp
                )
            )
        }
    }

    override suspend fun removeHistoryRecord(recordId: String) {
        val currentJson = localDataSource.getHistoryRecordsJson().first()
        val currentList = if (currentJson.isEmpty()) {
            emptyList()
        } else {
            parseHistoryRecords(currentJson)
        }
        val newList = currentList.filter { it.id != recordId }
        val newJson = serializeHistoryRecords(newList)
        localDataSource.setHistoryRecordsJson(newJson)

        // Note: Không xóa khỏi Room Database để giữ lại dữ liệu lịch sử
    }

    override suspend fun clearTodayHistory() {
        localDataSource.setHistoryRecordsJson("")
    }

    // ========================================================================
    // ✅ HISTORY SCREEN DATA (Dùng Room Database)
    // ========================================================================

    override fun getMonthlyData(year: Int, month: Int): Flow<Map<Int, Int>> = flow {
        val waterGoal = settingsRepository.getWaterGoal().first()
        val yearMonth = YearMonth.of(year, month)
        val daysInMonth = yearMonth.lengthOfMonth()

        // Lấy dữ liệu từ Room
        val startDate = LocalDate.of(year, month, 1).format(dateFormatter)
        val endDate = LocalDate.of(year, month, daysInMonth).format(dateFormatter)

        val dailySummaries = waterLogDao.getGroupedByDate(startDate, endDate)

        // Convert sang Map<Day, Percentage>
        val data = mutableMapOf<Int, Int>()
        dailySummaries.forEach { summary ->
            val date = LocalDate.parse(summary.date, dateFormatter)
            val day = date.dayOfMonth
            val percentage = ((summary.totalMl.toFloat() / waterGoal) * 100).toInt().coerceIn(0, 100)
            data[day] = percentage
        }

        emit(data)
    }

    override fun getYearlyData(year: Int): Flow<Map<Int, Int>> = flow {
        val waterGoal = settingsRepository.getWaterGoal().first()

        // Dữ liệu trung bình theo tháng
        val data = mutableMapOf<Int, Int>()

        for (month in 1..12) {
            val yearMonth = YearMonth.of(year, month)
            val daysInMonth = yearMonth.lengthOfMonth()

            val startDate = LocalDate.of(year, month, 1).format(dateFormatter)
            val endDate = LocalDate.of(year, month, daysInMonth).format(dateFormatter)

            val total = waterLogDao.getTotalByDateRange(startDate, endDate) ?: 0
            val avgDaily = if (daysInMonth > 0) total / daysInMonth else 0
            val percentage = ((avgDaily.toFloat() / waterGoal) * 100).toInt().coerceIn(0, 100)

            data[month] = percentage
        }

        emit(data)
    }

    override fun getWeeklyProgress(): Flow<List<DayProgress>> = flow {
        val waterGoal = settingsRepository.getWaterGoal().first()
        val today = LocalDate.now()

        // Lấy thứ 2 tuần này
        val weekFields = WeekFields.of(Locale.getDefault())
        val monday = today.with(weekFields.dayOfWeek(), 1)

        val weekProgress = mutableListOf<DayProgress>()
        val dayLabels = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")

        for (i in 0..6) {
            val date = monday.plusDays(i.toLong())
            val dateString = date.format(dateFormatter)
            val dayOfMonth = date.dayOfMonth

            val intake = waterLogDao.getTotalByDate(dateString) ?: 0
            val completed = intake >= waterGoal

            weekProgress.add(
                DayProgress(
                    label = dayLabels[i],
                    day = dayOfMonth,
                    intake = intake,
                    goal = waterGoal,
                    completed = completed
                )
            )
        }

        emit(weekProgress)
    }

    override fun getWaterStatistics(): Flow<WaterStatistics> = flow {
        val waterGoal = settingsRepository.getWaterGoal().first()
        val today = LocalDate.now()

        // ========================================
        // 1. TRUNG BÌNH TUẦN (7 ngày gần nhất)
        // ========================================
        val startOfWeek = today.minusDays(6)
        val weekStartDate = startOfWeek.format(dateFormatter)
        val weekEndDate = today.format(dateFormatter)

        val weekTotal = waterLogDao.getTotalByDateRange(weekStartDate, weekEndDate) ?: 0
        val weeklyAverage = weekTotal / 7

        // ========================================
        // 2. TRUNG BÌNH THÁNG (30 ngày gần nhất)
        // ========================================
        val startOfMonth = today.minusDays(29)
        val monthStartDate = startOfMonth.format(dateFormatter)
        val monthEndDate = today.format(dateFormatter)

        val monthTotal = waterLogDao.getTotalByDateRange(monthStartDate, monthEndDate) ?: 0
        val monthlyAverage = monthTotal / 30

        // ========================================
        // 3. TẦN SUẤT UỐNG NƯỚC (Tháng hiện tại)
        // ========================================
        val currentMonthStart = today.withDayOfMonth(1).format(dateFormatter)
        val currentMonthEnd = today.format(dateFormatter)

        val daysInCurrentMonth = today.dayOfMonth
        var totalDrinks = 0

        for (i in 0 until daysInCurrentMonth) {
            val date = today.minusDays(i.toLong()).format(dateFormatter)
            val count = waterLogDao.getCountByDate(date)
            totalDrinks += count
        }

        val drinkingFrequency = if (daysInCurrentMonth > 0) {
            totalDrinks / daysInCurrentMonth
        } else {
            0
        }

        // ========================================
        // 4. TỶ LỆ HOÀN THÀNH MỤC TIÊU (Tháng hiện tại)
        // ========================================
        var completedDays = 0

        for (i in 0 until daysInCurrentMonth) {
            val date = today.minusDays(i.toLong()).format(dateFormatter)
            val intake = waterLogDao.getTotalByDate(date) ?: 0
            if (intake >= waterGoal) {
                completedDays++
            }
        }

        val completionRate = if (daysInCurrentMonth > 0) {
            (completedDays * 100) / daysInCurrentMonth
        } else {
            0
        }

        emit(
            WaterStatistics(
                weeklyAverage = weeklyAverage,
                monthlyAverage = monthlyAverage,
                completionRate = completionRate,
                drinkingFrequency = drinkingFrequency
            )
        )
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================
    private fun parseHistoryRecords(json: String): List<HistoryRecord> {
        return try {
            val jsonArray = JSONArray(json)
            (0 until jsonArray.length()).map { i ->
                val obj = jsonArray.getJSONObject(i)
                HistoryRecord(
                    id = obj.getString("id"),
                    time = obj.getString("time"),
                    type = obj.getString("type"),
                    amount = obj.getString("amount"),
                    icon = obj.getString("icon"),
                    timestamp = obj.getLong("timestamp")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun serializeHistoryRecords(records: List<HistoryRecord>): String {
        val jsonArray = JSONArray()
        records.forEach { record ->
            val obj = JSONObject().apply {
                put("id", record.id)
                put("time", record.time)
                put("type", record.type)
                put("amount", record.amount)
                put("icon", record.icon)
                put("timestamp", record.timestamp)
            }
            jsonArray.put(obj)
        }
        return jsonArray.toString()
    }
}