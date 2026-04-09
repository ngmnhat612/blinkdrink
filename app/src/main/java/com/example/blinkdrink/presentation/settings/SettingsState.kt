// ============================================================================
// FILE: presentation/settings/SettingsState.kt
// ============================================================================
package com.example.blinkdrink.presentation.settings

data class SettingsState(
    val eyeBreakFrequency: Int = 2,
    val eyeBreakDuration: Int = 3, // ⭐ THÊM - Thời gian nghỉ mắt (phút)
    val waterGoal: Int = 2530,
    val gender: String = "Nam",
    val weight: Int = 60,
    val wakeTime: String = "06:00",
    val bedTime: String = "22:00"
)