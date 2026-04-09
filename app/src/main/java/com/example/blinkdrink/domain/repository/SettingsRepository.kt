// ============================================================================
// FILE: domain/repository/SettingsRepository.kt
// ============================================================================
package com.example.blinkdrink.domain.repository

import com.example.blinkdrink.domain.model.ReminderTime
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getEyeBreakFrequency(): Flow<Int>
    suspend fun setEyeBreakFrequency(frequency: Int)

    // ⭐ THÊM - Eye Break Duration
    fun getEyeBreakDuration(): Flow<Int>
    suspend fun setEyeBreakDuration(minutes: Int)

    fun getWaterSchedule(): Flow<List<ReminderTime>>
    suspend fun saveWaterSchedule(schedule: List<ReminderTime>)

    fun getWaterGoal(): Flow<Int>
    suspend fun setWaterGoal(goal: Int)

    fun getGender(): Flow<String>
    suspend fun setGender(gender: String)

    fun getWeight(): Flow<Int>
    suspend fun setWeight(weight: Int)

    fun getWakeTime(): Flow<String>
    suspend fun setWakeTime(time: String)

    fun getBedTime(): Flow<String>
    suspend fun setBedTime(time: String)

    fun getSelectedWaterCupSize(): Flow<Int>
    suspend fun setSelectedWaterCupSize(size: Int)

    fun getDailyWaterIntake(): Flow<Int>
    suspend fun setDailyWaterIntake(amount: Int)

    fun getLastResetDate(): Flow<String>
    suspend fun setLastResetDate(date: String)

    fun getDailyEyeBreakCount(): Flow<Int>
    suspend fun setDailyEyeBreakCount(count: Int)

    fun getAdjustedDailyWaterGoal(): Flow<Int>
    suspend fun setAdjustedDailyWaterGoal(goal: Int)

    fun getHasAdjustedToday(): Flow<Boolean>
    suspend fun setHasAdjustedToday(hasAdjusted: Boolean)

    // ⭐ THÊM MỚI - Eye Break Session
    fun getEyeBreakSessionActive(): Flow<Boolean>
    suspend fun setEyeBreakSessionActive(isActive: Boolean)

    fun getEyeBreakSessionDuration(): Flow<Int>
    suspend fun setEyeBreakSessionDuration(minutes: Int)

    fun getEyeBreakSessionStartTime(): Flow<Long>
    suspend fun setEyeBreakSessionStartTime(timestamp: Long)

    fun getEyeBreakOnBreak(): Flow<Boolean>
    suspend fun setEyeBreakOnBreak(isOnBreak: Boolean)

    fun getEyeBreakBreakStartTime(): Flow<Long>
    suspend fun setEyeBreakBreakStartTime(timestamp: Long)

    // ✅ Methods mới
    suspend fun setExpectedBreakCount(count: Int)
    fun getExpectedBreakCount(): Flow<Int>
}