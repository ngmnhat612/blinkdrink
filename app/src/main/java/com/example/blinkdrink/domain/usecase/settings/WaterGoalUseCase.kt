// ============================================================================
// FILE: domain/usecase/settings/WaterGoalUseCases.kt (GOM NHÓM WATER)
// ============================================================================
package com.example.blinkdrink.domain.usecase.settings

import com.example.blinkdrink.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * 🎯 WATER GOAL - Quản lý mục tiêu nước
 */

// Base Goal (từ onboarding hoặc settings)
class GetWaterGoalUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Int> = repository.getWaterGoal()
}

class UpdateWaterGoalUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(goal: Int) {
        repository.setWaterGoal(goal)
    }
}

// Adjusted Goal (có thể thay đổi theo thời tiết)
class GetAdjustedWaterGoalUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Int> = repository.getAdjustedDailyWaterGoal()
}

class UpdateAdjustedWaterGoalUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(goal: Int) {
        repository.setAdjustedDailyWaterGoal(goal)
    }
}

// ⭐ Business Logic UseCase
class AdjustWaterGoalByWeatherUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    /**
     * Điều chỉnh mục tiêu nước theo thời tiết (1 lần/ngày)
     *
     * @param adjustment Số ml cần thêm/bớt (có thể âm)
     * @return true nếu thành công, false nếu đã điều chỉnh rồi
     */
    suspend operator fun invoke(adjustment: Int): Boolean {
        val hasAdjustedToday = repository.getHasAdjustedToday().first()

        if (hasAdjustedToday) {
            return false // Đã điều chỉnh rồi
        }

        val currentGoal = repository.getAdjustedDailyWaterGoal().first()
        val baseGoal = repository.getWaterGoal().first()

        val actualCurrentGoal = if (currentGoal == 0) baseGoal else currentGoal
        val newGoal = (actualCurrentGoal + adjustment).coerceAtLeast(500)

        repository.setAdjustedDailyWaterGoal(newGoal)
        repository.setHasAdjustedToday(true)

        return true
    }
}

class GetHasAdjustedTodayUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.getHasAdjustedToday()
}