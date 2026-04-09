// ============================================================================
// FILE: domain/usecase/history/RemoveHistoryRecordUseCase.kt
// ============================================================================
package com.example.blinkdrink.domain.usecase.history

import com.example.blinkdrink.domain.repository.HistoryRepository
import javax.inject.Inject

class RemoveHistoryRecordUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(recordId: String) {
        repository.removeHistoryRecord(recordId)
    }
}