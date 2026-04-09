// ============================================================================
// FILE: domain/usecase/settings/DailyTrackingUseCases.kt (GOM NHÓM DAILY)
// ============================================================================
package com.example.blinkdrink.domain.usecase.settings

import com.example.blinkdrink.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * 📊 DAILY TRACKING - Theo dõi hàng ngày
 */

// Water Intake
class GetDailyWaterIntakeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Int> = repository.getDailyWaterIntake()
}

class UpdateDailyWaterIntakeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(amount: Int) {
        repository.setDailyWaterIntake(amount)
    }
}

// Eye Break Count
class GetDailyEyeBreakCountUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Int> = repository.getDailyEyeBreakCount()
}

class UpdateDailyEyeBreakCountUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(count: Int) {
        repository.setDailyEyeBreakCount(count)
    }
}

// ⭐ Business Logic: Reset Daily Data
class CheckAndResetDailyDataUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(weatherAdjustment: Int = 0) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastResetDate = repository.getLastResetDate().first()

        if (lastResetDate != today) {
            // Reset dữ liệu ngày mới
            repository.setDailyWaterIntake(0)
            repository.setDailyEyeBreakCount(0)

            // Cập nhật mục tiêu mới
            val baseGoal = repository.getWaterGoal().first()
            val newAdjustedGoal = baseGoal + weatherAdjustment
            repository.setAdjustedDailyWaterGoal(newAdjustedGoal)

            // Reset flag điều chỉnh
            repository.setHasAdjustedToday(false)

            // Lưu ngày reset
            repository.setLastResetDate(today)
        }
    }
}