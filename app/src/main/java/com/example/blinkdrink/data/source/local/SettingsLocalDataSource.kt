// ============================================================================
// FILE: data/source/local/SettingsLocalDataSource.kt
// ============================================================================
package com.example.blinkdrink.data.source.local

import com.example.blinkdrink.data.source.local.datastore.DataStoreManager
import com.example.blinkdrink.data.source.local.datastore.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsLocalDataSource @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    // Eye Break Frequency
    fun getEyeBreakFrequency(): Flow<Int> {
        return dataStoreManager.getValue(PreferencesKeys.EYE_BREAK_FREQUENCY, 2)
    }

    suspend fun setEyeBreakFrequency(frequency: Int) {
        dataStoreManager.setValue(PreferencesKeys.EYE_BREAK_FREQUENCY, frequency)
    }

    // ⭐ Eye Break Duration (MỚI)
    fun getEyeBreakDuration(): Flow<Int> {
        return dataStoreManager.getValue(PreferencesKeys.EYE_BREAK_DURATION, 3)
    }

    suspend fun setEyeBreakDuration(minutes: Int) {
        dataStoreManager.setValue(PreferencesKeys.EYE_BREAK_DURATION, minutes)
    }

    // Water Schedule
    fun getWaterScheduleJson(): Flow<String> {
        return dataStoreManager.getValue(PreferencesKeys.WATER_SCHEDULE_JSON, "")
    }

    suspend fun setWaterScheduleJson(json: String) {
        dataStoreManager.setValue(PreferencesKeys.WATER_SCHEDULE_JSON, json)
    }

    // Selected Water Cup Size
    fun getSelectedWaterCupSize(): Flow<Int> {
        return dataStoreManager.getValue(PreferencesKeys.SELECTED_WATER_CUP_SIZE, 100)
    }

    suspend fun setSelectedWaterCupSize(size: Int) {
        dataStoreManager.setValue(PreferencesKeys.SELECTED_WATER_CUP_SIZE, size)
    }

    // Daily Water Intake
    fun getDailyWaterIntake(): Flow<Int> {
        return dataStoreManager.getValue(PreferencesKeys.DAILY_WATER_INTAKE, 0)
    }

    suspend fun setDailyWaterIntake(amount: Int) {
        dataStoreManager.setValue(PreferencesKeys.DAILY_WATER_INTAKE, amount)
    }

    // Last Reset Date
    fun getLastResetDate(): Flow<String> {
        return dataStoreManager.getValue(PreferencesKeys.LAST_RESET_DATE, "")
    }

    suspend fun setLastResetDate(date: String) {
        dataStoreManager.setValue(PreferencesKeys.LAST_RESET_DATE, date)
    }

    // Daily Eye Break Count
    fun getDailyEyeBreakCount(): Flow<Int> {
        return dataStoreManager.getValue(PreferencesKeys.DAILY_EYE_BREAK_COUNT, 0)
    }

    suspend fun setDailyEyeBreakCount(count: Int) {
        dataStoreManager.setValue(PreferencesKeys.DAILY_EYE_BREAK_COUNT, count)
    }

    // Adjusted Daily Water Goal
    fun getAdjustedDailyWaterGoal(): Flow<Int> {
        return dataStoreManager.getValue(PreferencesKeys.ADJUSTED_DAILY_WATER_GOAL, 0)
    }

    suspend fun setAdjustedDailyWaterGoal(goal: Int) {
        dataStoreManager.setValue(PreferencesKeys.ADJUSTED_DAILY_WATER_GOAL, goal)
    }

    // Has Adjusted Today Flag
    fun getHasAdjustedToday(): Flow<Boolean> {
        return dataStoreManager.getValue(PreferencesKeys.HAS_ADJUSTED_TODAY, false)
    }

    suspend fun setHasAdjustedToday(hasAdjusted: Boolean) {
        dataStoreManager.setValue(PreferencesKeys.HAS_ADJUSTED_TODAY, hasAdjusted)
    }

    // ⭐ THÊM MỚI - Eye Break Session Methods
    fun getEyeBreakSessionActive(): Flow<Boolean> {
        return dataStoreManager.getValue(PreferencesKeys.EYE_BREAK_SESSION_ACTIVE, false)
    }

    suspend fun setEyeBreakSessionActive(isActive: Boolean) {
        dataStoreManager.setValue(PreferencesKeys.EYE_BREAK_SESSION_ACTIVE, isActive)
    }

    fun getEyeBreakSessionDuration(): Flow<Int> {
        return dataStoreManager.getValue(PreferencesKeys.EYE_BREAK_SESSION_DURATION, 0)
    }

    suspend fun setEyeBreakSessionDuration(minutes: Int) {
        dataStoreManager.setValue(PreferencesKeys.EYE_BREAK_SESSION_DURATION, minutes)
    }

    fun getEyeBreakSessionStartTime(): Flow<Long> {
        return dataStoreManager.getValue(PreferencesKeys.EYE_BREAK_SESSION_START_TIME, 0L)
    }

    suspend fun setEyeBreakSessionStartTime(timestamp: Long) {
        dataStoreManager.setValue(PreferencesKeys.EYE_BREAK_SESSION_START_TIME, timestamp)
    }

    fun getEyeBreakOnBreak(): Flow<Boolean> {
        return dataStoreManager.getValue(PreferencesKeys.EYE_BREAK_ON_BREAK, false)
    }

    suspend fun setEyeBreakOnBreak(isOnBreak: Boolean) {
        dataStoreManager.setValue(PreferencesKeys.EYE_BREAK_ON_BREAK, isOnBreak)
    }

    fun getEyeBreakBreakStartTime(): Flow<Long> {
        return dataStoreManager.getValue(PreferencesKeys.EYE_BREAK_BREAK_START_TIME, 0L)
    }

    suspend fun setEyeBreakBreakStartTime(timestamp: Long) {
        dataStoreManager.setValue(PreferencesKeys.EYE_BREAK_BREAK_START_TIME, timestamp)
    }

    // Expected Break Count
    fun getExpectedBreakCount(): Flow<Int> {
        return dataStoreManager.getValue(PreferencesKeys.EXPECTED_BREAK_COUNT, 0)
    }

    suspend fun setExpectedBreakCount(count: Int) {
        dataStoreManager.setValue(PreferencesKeys.EXPECTED_BREAK_COUNT, count)
    }

}