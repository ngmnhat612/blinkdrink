// ============================================================================
// FILE: domain/usecase/user/SaveOnboardingDataUseCase.kt
// ============================================================================
package com.example.blinkdrink.domain.usecase.user

import com.example.blinkdrink.domain.model.Gender
import com.example.blinkdrink.domain.model.UserProfile
import com.example.blinkdrink.domain.repository.UserRepository
import javax.inject.Inject

class SaveOnboardingDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        gender: Gender,
        weightKg: Int,
        wakeUpHour: Int,
        wakeUpMinute: Int,
        sleepHour: Int,
        sleepMinute: Int
    ) {
        // Tính toán lượng nước khuyến nghị dựa trên cân nặng và giới tính
        val dailyWaterGoal = calculateDailyWaterGoal(weightKg, gender)

        val profile = UserProfile(
            gender = gender,
            weightKg = weightKg,
            wakeUpHour = wakeUpHour,
            wakeUpMinute = wakeUpMinute,
            sleepHour = sleepHour,
            sleepMinute = sleepMinute,
            dailyWaterGoalMl = dailyWaterGoal,
            isOnboardingCompleted = true
        )

        userRepository.saveUserProfile(profile)
        userRepository.completeOnboarding()
    }

    private fun calculateDailyWaterGoal(weightKg: Int, gender: Gender): Int {
        // Công thức: 30-35ml/kg cho nam, 25-30ml/kg cho nữ
        val multiplier = if (gender == Gender.MALE) 35 else 30
        return weightKg * multiplier
    }
}