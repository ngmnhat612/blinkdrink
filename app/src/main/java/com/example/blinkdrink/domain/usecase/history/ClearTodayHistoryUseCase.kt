// ============================================================================
// FILE: domain/usecase/history/ClearTodayHistoryUseCase.kt
// ============================================================================
package com.example.blinkdrink.domain.usecase.history

import com.example.blinkdrink.domain.repository.HistoryRepository
import javax.inject.Inject

class ClearTodayHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke() {
        repository.clearTodayHistory()
    }
}