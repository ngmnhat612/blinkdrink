// ============================================================================
// FILE: presentation/history/HistoryState.kt
// ============================================================================
package com.example.blinkdrink.presentation.history

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.blinkdrink.domain.model.DayProgress
import com.example.blinkdrink.domain.model.WaterStatistics
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class HistoryState(
    val selectedTab: Int = 0, // 0 = Tháng, 1 = Năm
    val currentDate: LocalDate = LocalDate.now(),
    val chartData: Map<Int, Int> = emptyMap(),
    val weeklyProgress: List<DayProgress> = emptyList(),
    val statistics: WaterStatistics = WaterStatistics(0, 0, 0, 0),
    val isLoading: Boolean = false
)