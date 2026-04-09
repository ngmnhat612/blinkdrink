// ============================================================================
// FILE: domain/usecase/history/GetTodayHistoryUseCase.kt
// ============================================================================
package com.example.blinkdrink.domain.usecase.history

import com.example.blinkdrink.domain.model.HistoryRecord
import com.example.blinkdrink.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke(): Flow<List<HistoryRecord>> {
        return repository.getTodayHistory()
    }
}