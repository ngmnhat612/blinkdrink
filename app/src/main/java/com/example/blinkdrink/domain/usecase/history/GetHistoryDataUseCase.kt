// ============================================================================
// FILE: domain/usecase/history/GetHistoryDataUseCase.kt
// ============================================================================
package com.example.blinkdrink.domain.usecase.history

import com.example.blinkdrink.domain.model.DayProgress
import com.example.blinkdrink.domain.model.WaterStatistics
import com.example.blinkdrink.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMonthlyDataUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke(year: Int, month: Int): Flow<Map<Int, Int>> {
        return repository.getMonthlyData(year, month)
    }
}

class GetYearlyDataUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke(year: Int): Flow<Map<Int, Int>> {
        return repository.getYearlyData(year)
    }
}

class GetWeeklyProgressUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke(): Flow<List<DayProgress>> {
        return repository.getWeeklyProgress()
    }
}

class GetWaterStatisticsUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke(): Flow<WaterStatistics> {
        return repository.getWaterStatistics()
    }
}
