// ============================================================================
// FILE: domain/usecase/settings/WaterCupUseCases.kt (GOM NHÓM CỐC NƯỚC)
// ============================================================================
package com.example.blinkdrink.domain.usecase.settings

import com.example.blinkdrink.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 🥤 WATER CUP - Quản lý kích cỡ cốc
 */

class GetSelectedWaterCupSizeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Int> = repository.getSelectedWaterCupSize()
}

class UpdateSelectedWaterCupSizeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(size: Int) {
        repository.setSelectedWaterCupSize(size)
    }
}