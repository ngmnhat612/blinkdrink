// ============================================================================
// FILE: domain/usecase/settings/EyeBreakSessionUseCases.kt
// ============================================================================
package com.example.blinkdrink.domain.usecase.settings

import com.example.blinkdrink.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ⏱️ EYE BREAK SESSION - Quản lý phiên làm việc
 */

// Session Active State
class GetEyeBreakSessionActiveUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.getEyeBreakSessionActive()
}

class SetEyeBreakSessionActiveUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(isActive: Boolean) {
        repository.setEyeBreakSessionActive(isActive)
    }
}

// Session Duration
class GetEyeBreakSessionDurationUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Int> = repository.getEyeBreakSessionDuration()
}

class SetEyeBreakSessionDurationUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(minutes: Int) {
        repository.setEyeBreakSessionDuration(minutes)
    }
}

// Session Start Time
class GetEyeBreakSessionStartTimeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Long> = repository.getEyeBreakSessionStartTime()
}

class SetEyeBreakSessionStartTimeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(timestamp: Long) {
        repository.setEyeBreakSessionStartTime(timestamp)
    }
}

// Break State (đang nghỉ hay không)
class GetEyeBreakOnBreakUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.getEyeBreakOnBreak()
}

class SetEyeBreakOnBreakUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(isOnBreak: Boolean) {
        repository.setEyeBreakOnBreak(isOnBreak)
    }
}

// Break Start Time
class GetEyeBreakBreakStartTimeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Long> = repository.getEyeBreakBreakStartTime()
}

class SetEyeBreakBreakStartTimeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(timestamp: Long) {
        repository.setEyeBreakBreakStartTime(timestamp)
    }
}