// ============================================================================
// FILE: presentation/onboarding/OnboardingState.kt
// ============================================================================
package com.example.blinkdrink.presentation.onboarding

import com.example.blinkdrink.domain.model.Gender

data class OnboardingState(
    val gender: Gender = Gender.MALE,
    val weightKg: Int = 70,
    val wakeUpHour: Int = 7,
    val wakeUpMinute: Int = 0,
    val sleepHour: Int = 23,
    val sleepMinute: Int = 0,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)