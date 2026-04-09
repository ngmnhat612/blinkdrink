// ============================================================================
// FILE: domain/model/UserProfile.kt
// ============================================================================
package com.example.blinkdrink.domain.model

data class UserProfile(
    val gender: Gender,
    val weightKg: Int,
    val wakeUpHour: Int,
    val wakeUpMinute: Int,
    val sleepHour: Int,
    val sleepMinute: Int,
    val dailyWaterGoalMl: Int = 2000, // Mặc định
    val isOnboardingCompleted: Boolean = false
)

enum class Gender {
    MALE, FEMALE
}