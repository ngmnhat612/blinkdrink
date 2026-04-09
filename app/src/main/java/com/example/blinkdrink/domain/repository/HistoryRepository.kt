// ============================================================================
// FILE: domain/repository/HistoryRepository.kt
// ============================================================================
package com.example.blinkdrink.domain.repository

import com.example.blinkdrink.domain.model.DayProgress
import com.example.blinkdrink.domain.model.HistoryRecord
import com.example.blinkdrink.domain.model.WaterStatistics
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    // Today history (đã có)
    fun getTodayHistory(): Flow<List<HistoryRecord>>
    suspend fun addHistoryRecord(record: HistoryRecord)
    suspend fun removeHistoryRecord(recordId: String)
    suspend fun clearTodayHistory()

    // ⭐ THÊM MỚI - Cho History Screen
    fun getMonthlyData(year: Int, month: Int): Flow<Map<Int, Int>>
    fun getYearlyData(year: Int): Flow<Map<Int, Int>>
    fun getWeeklyProgress(): Flow<List<DayProgress>>
    fun getWaterStatistics(): Flow<WaterStatistics>
}