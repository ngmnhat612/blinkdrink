// ============================================================================
// FILE: presentation/dashboard/DashboardState.kt
// ============================================================================
package com.example.blinkdrink.presentation.dashboard

import com.example.blinkdrink.domain.model.HistoryRecord
import com.example.blinkdrink.domain.model.WeatherInfo

data class DashboardState(
    val currentIntake: Int = 0,
    val dailyWaterGoal: Int = 2500,
    val baseWaterGoal: Int = 2500,
    val selectedWaterAmount: Int = 100,
    val eyeRestCount: Int = 0,
    val dailyEyeGoal: Int = 120,
    val weatherInfo: WeatherInfo = WeatherInfo(),
    val isLoadingWeather: Boolean = true,
    val historyList: List<HistoryRecord> = emptyList(),
    val hasAdjustedToday: Boolean = false,

    // ⭐ THÊM - Eye Break Session
    val isEyeBreakSessionActive: Boolean = false,
    val eyeBreakSessionDurationMinutes: Int = 0,
    val eyeBreakRemainingSeconds: Int = 0,
    val isOnEyeBreak: Boolean = false, // Đang trong thời gian nghỉ
    val eyeBreakFrequency: Int = 2, // Tần suất nghỉ mắt (lần/giờ)
    val eyeBreakDurationMinutes: Int = 3, // Thời gian nghỉ (phút)
    val sessionRemainingSeconds: Int = 0 // Thời gian còn lại của phiên (giây)
)