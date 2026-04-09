// ============================================================================
// FILE: data/repository/SettingsRepositoryImpl.kt
// ============================================================================
package com.example.blinkdrink.data.repository

import com.example.blinkdrink.data.source.local.SettingsLocalDataSource
import com.example.blinkdrink.data.source.local.UserLocalDataSource
import com.example.blinkdrink.domain.model.ReminderTime
import com.example.blinkdrink.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : SettingsRepository {

    // Eye Break Frequency
    override fun getEyeBreakFrequency(): Flow<Int> {
        return settingsLocalDataSource.getEyeBreakFrequency()
    }

    override suspend fun setEyeBreakFrequency(frequency: Int) {
        settingsLocalDataSource.setEyeBreakFrequency(frequency)
    }

    // ⭐ Eye Break Duration (MỚI)
    override fun getEyeBreakDuration(): Flow<Int> {
        return settingsLocalDataSource.getEyeBreakDuration()
    }

    override suspend fun setEyeBreakDuration(minutes: Int) {
        settingsLocalDataSource.setEyeBreakDuration(minutes)
    }

    // Water Schedule
    override fun getWaterSchedule(): Flow<List<ReminderTime>> {
        return settingsLocalDataSource.getWaterScheduleJson().map { json ->
            if (json.isEmpty()) {
                getDefaultWaterSchedule()
            } else {
                parseWaterSchedule(json)
            }
        }
    }

    override suspend fun saveWaterSchedule(schedule: List<ReminderTime>) {
        val json = serializeWaterSchedule(schedule)
        settingsLocalDataSource.setWaterScheduleJson(json)
    }

    // Water Goal
    override fun getWaterGoal(): Flow<Int> {
        return userLocalDataSource.getUserProfile().map { it?.dailyWaterGoalMl ?: 2530 }
    }

    override suspend fun setWaterGoal(goal: Int) {
        val profile = userLocalDataSource.getUserProfile().first()
        profile?.let {
            userLocalDataSource.saveUserProfile(it.copy(dailyWaterGoalMl = goal))
        }
    }

    // Gender
    override fun getGender(): Flow<String> {
        return userLocalDataSource.getUserProfile().map {
            if (it?.gender == com.example.blinkdrink.domain.model.Gender.MALE) "Nam" else "Nữ"
        }
    }

    override suspend fun setGender(gender: String) {
        val profile = userLocalDataSource.getUserProfile().first()
        profile?.let {
            val genderEnum = if (gender == "Nam") {
                com.example.blinkdrink.domain.model.Gender.MALE
            } else {
                com.example.blinkdrink.domain.model.Gender.FEMALE
            }
            userLocalDataSource.saveUserProfile(it.copy(gender = genderEnum))
        }
    }

    // Weight
    override fun getWeight(): Flow<Int> {
        return userLocalDataSource.getUserProfile().map { it?.weightKg ?: 60 }
    }

    override suspend fun setWeight(weight: Int) {
        val profile = userLocalDataSource.getUserProfile().first()
        profile?.let {
            userLocalDataSource.saveUserProfile(it.copy(weightKg = weight))
        }
    }

    // Wake Time
    override fun getWakeTime(): Flow<String> {
        return userLocalDataSource.getUserProfile().map { profile ->
            profile?.let {
                String.format("%02d:%02d", it.wakeUpHour, it.wakeUpMinute)
            } ?: "06:00"
        }
    }

    override suspend fun setWakeTime(time: String) {
        val parts = time.split(":")
        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 6
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0

        val profile = userLocalDataSource.getUserProfile().first()
        profile?.let {
            userLocalDataSource.saveUserProfile(
                it.copy(wakeUpHour = hour, wakeUpMinute = minute)
            )
        }
    }

    // Bed Time
    override fun getBedTime(): Flow<String> {
        return userLocalDataSource.getUserProfile().map { profile ->
            profile?.let {
                String.format("%02d:%02d", it.sleepHour, it.sleepMinute)
            } ?: "22:00"
        }
    }

    override suspend fun setBedTime(time: String) {
        val parts = time.split(":")
        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 22
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0

        val profile = userLocalDataSource.getUserProfile().first()
        profile?.let {
            userLocalDataSource.saveUserProfile(
                it.copy(sleepHour = hour, sleepMinute = minute)
            )
        }
    }

    // Selected Water Cup Size
    override fun getSelectedWaterCupSize(): Flow<Int> {
        return settingsLocalDataSource.getSelectedWaterCupSize()
    }

    override suspend fun setSelectedWaterCupSize(size: Int) {
        settingsLocalDataSource.setSelectedWaterCupSize(size)
    }

    // Daily Water Intake
    override fun getDailyWaterIntake(): Flow<Int> {
        return settingsLocalDataSource.getDailyWaterIntake()
    }

    override suspend fun setDailyWaterIntake(amount: Int) {
        settingsLocalDataSource.setDailyWaterIntake(amount)
    }

    // Last Reset Date
    override fun getLastResetDate(): Flow<String> {
        return settingsLocalDataSource.getLastResetDate()
    }

    override suspend fun setLastResetDate(date: String) {
        settingsLocalDataSource.setLastResetDate(date)
    }

    // Daily Eye Break Count
    override fun getDailyEyeBreakCount(): Flow<Int> {
        return settingsLocalDataSource.getDailyEyeBreakCount()
    }

    override suspend fun setDailyEyeBreakCount(count: Int) {
        settingsLocalDataSource.setDailyEyeBreakCount(count)
    }

    // Adjusted Daily Water Goal
    override fun getAdjustedDailyWaterGoal(): Flow<Int> {
        return settingsLocalDataSource.getAdjustedDailyWaterGoal()
    }

    override suspend fun setAdjustedDailyWaterGoal(goal: Int) {
        settingsLocalDataSource.setAdjustedDailyWaterGoal(goal)
    }

    // Has Adjusted Today Flag
    override fun getHasAdjustedToday(): Flow<Boolean> {
        return settingsLocalDataSource.getHasAdjustedToday()
    }

    override suspend fun setHasAdjustedToday(hasAdjusted: Boolean) {
        settingsLocalDataSource.setHasAdjustedToday(hasAdjusted)
    }

    // ⭐ THÊM MỚI - Eye Break Session Implementation
    override fun getEyeBreakSessionActive(): Flow<Boolean> {
        return settingsLocalDataSource.getEyeBreakSessionActive()
    }

    override suspend fun setEyeBreakSessionActive(isActive: Boolean) {
        settingsLocalDataSource.setEyeBreakSessionActive(isActive)
    }

    override fun getEyeBreakSessionDuration(): Flow<Int> {
        return settingsLocalDataSource.getEyeBreakSessionDuration()
    }

    override suspend fun setEyeBreakSessionDuration(minutes: Int) {
        settingsLocalDataSource.setEyeBreakSessionDuration(minutes)
    }

    override fun getEyeBreakSessionStartTime(): Flow<Long> {
        return settingsLocalDataSource.getEyeBreakSessionStartTime()
    }

    override suspend fun setEyeBreakSessionStartTime(timestamp: Long) {
        settingsLocalDataSource.setEyeBreakSessionStartTime(timestamp)
    }

    override fun getEyeBreakOnBreak(): Flow<Boolean> {
        return settingsLocalDataSource.getEyeBreakOnBreak()
    }

    override suspend fun setEyeBreakOnBreak(isOnBreak: Boolean) {
        settingsLocalDataSource.setEyeBreakOnBreak(isOnBreak)
    }

    override fun getEyeBreakBreakStartTime(): Flow<Long> {
        return settingsLocalDataSource.getEyeBreakBreakStartTime()
    }

    override suspend fun setEyeBreakBreakStartTime(timestamp: Long) {
        settingsLocalDataSource.setEyeBreakBreakStartTime(timestamp)
    }

    // Expected Break Count
    override fun getExpectedBreakCount(): Flow<Int> {
        return settingsLocalDataSource.getExpectedBreakCount()
    }

    override suspend fun setExpectedBreakCount(count: Int) {
        settingsLocalDataSource.setExpectedBreakCount(count)
    }


    // ========================================================================
    // HELPER FUNCTIONS
    // ========================================================================
    private fun getDefaultWaterSchedule(): List<ReminderTime> {
        return listOf(
            ReminderTime(1, 6, 0, true),
            ReminderTime(2, 7, 30, true),
            ReminderTime(3, 9, 0, true),
            ReminderTime(4, 10, 30, true),
            ReminderTime(5, 12, 0, true),
            ReminderTime(6, 13, 30, true),
            ReminderTime(7, 15, 0, true),
            ReminderTime(8, 16, 30, true),
            ReminderTime(9, 18, 0, true)
        )
    }

    private fun serializeWaterSchedule(schedule: List<ReminderTime>): String {
        return schedule.joinToString("|") { item ->
            "${item.id},${item.hour},${item.minute},${item.isEnabled}"
        }
    }

    private fun parseWaterSchedule(json: String): List<ReminderTime> {
        return try {
            json.split("|").map { str ->
                val parts = str.split(",")
                ReminderTime(
                    id = parts[0].toInt(),
                    hour = parts[1].toInt(),
                    minute = parts[2].toInt(),
                    isEnabled = parts[3].toBoolean()
                )
            }
        } catch (e: Exception) {
            getDefaultWaterSchedule()
        }
    }
}