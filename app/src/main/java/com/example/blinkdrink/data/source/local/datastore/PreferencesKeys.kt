// ============================================================================
// FILE: data/source/local/datastore/PreferencesKeys.kt
// ============================================================================
package com.example.blinkdrink.data.source.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    // User Profile Keys
    val USER_GENDER = stringPreferencesKey("user_gender")
    val USER_WEIGHT = intPreferencesKey("user_weight")
    val WAKE_UP_HOUR = intPreferencesKey("wake_up_hour")
    val WAKE_UP_MINUTE = intPreferencesKey("wake_up_minute")
    val SLEEP_HOUR = intPreferencesKey("sleep_hour")
    val SLEEP_MINUTE = intPreferencesKey("sleep_minute")
    val DAILY_WATER_GOAL = intPreferencesKey("daily_water_goal")
    val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")

    // Settings Keys
    val EYE_BREAK_FREQUENCY = intPreferencesKey("eye_break_frequency")
    val EYE_BREAK_DURATION = intPreferencesKey("eye_break_duration")
    val WATER_SCHEDULE_JSON = stringPreferencesKey("water_schedule_list_json")

    // Water Intake Keys (Dashboard)
    val SELECTED_WATER_CUP_SIZE = intPreferencesKey("selected_water_cup_size")
    val DAILY_WATER_INTAKE = intPreferencesKey("daily_water_intake")
    val LAST_RESET_DATE = stringPreferencesKey("last_reset_date")

    val ADJUSTED_DAILY_WATER_GOAL = intPreferencesKey("adjusted_daily_water_goal")
    val HAS_ADJUSTED_TODAY = booleanPreferencesKey("has_adjusted_today")

    // Eye Break Count
    val DAILY_EYE_BREAK_COUNT = intPreferencesKey("daily_eye_break_count")

    // ⭐ THÊM MỚI - Eye Break Session Persistence
    val EYE_BREAK_SESSION_ACTIVE = booleanPreferencesKey("eye_break_session_active")
    val EYE_BREAK_SESSION_DURATION = intPreferencesKey("eye_break_session_duration_minutes")
    val EYE_BREAK_SESSION_START_TIME = longPreferencesKey("eye_break_session_start_time")
    val EYE_BREAK_ON_BREAK = booleanPreferencesKey("eye_break_on_break")

    // ✅ THAY ĐỔI - Lưu timestamp bắt đầu nghỉ thay vì remaining seconds
    val EYE_BREAK_BREAK_START_TIME = longPreferencesKey("eye_break_break_start_time")

    // Thêm vào file PreferencesKeys.kt hiện tại:
    val HISTORY_RECORDS_JSON = stringPreferencesKey("history_records_json")

    // ✅ Key mới để lưu số lần expected THỰC TẾ
    val EXPECTED_BREAK_COUNT = intPreferencesKey("expected_break_count")
}