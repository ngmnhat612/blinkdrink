// ============================================================================
// FILE: domain/usecase/settings/EyeBreakSettingsUseCases.kt (GOM NHÓM EYE BREAK SETTINGS)
// ============================================================================
package com.example.blinkdrink.domain.usecase.settings

import com.example.blinkdrink.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 👁️ EYE BREAK SETTINGS - Cài đặt nghỉ mắt
 */

// Frequency (lần/giờ)
class GetEyeBreakFrequencyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Int> = repository.getEyeBreakFrequency()
}

class UpdateEyeBreakFrequencyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(frequency: Int) {
        repository.setEyeBreakFrequency(frequency)
    }
}

// Duration (phút)
class GetEyeBreakDurationUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Int> = repository.getEyeBreakDuration()
}

class UpdateEyeBreakDurationUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(minutes: Int) {
        repository.setEyeBreakDuration(minutes)
    }
}