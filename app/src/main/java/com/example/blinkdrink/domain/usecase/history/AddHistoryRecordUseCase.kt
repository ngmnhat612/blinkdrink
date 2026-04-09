// ============================================================================
// FILE: domain/usecase/history/AddHistoryRecordUseCase.kt
// ============================================================================
package com.example.blinkdrink.domain.usecase.history

import com.example.blinkdrink.domain.model.HistoryRecord
import com.example.blinkdrink.domain.repository.HistoryRepository
import javax.inject.Inject

class AddHistoryRecordUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(record: HistoryRecord) {
        repository.addHistoryRecord(record)
    }
}