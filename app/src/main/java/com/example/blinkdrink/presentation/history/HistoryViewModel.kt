// ============================================================================
// FILE: presentation/history/HistoryViewModel.kt
// ============================================================================
package com.example.blinkdrink.presentation.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkdrink.domain.usecase.history.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getMonthlyDataUseCase: GetMonthlyDataUseCase,
    private val getYearlyDataUseCase: GetYearlyDataUseCase,
    private val getWeeklyProgressUseCase: GetWeeklyProgressUseCase,
    private val getWaterStatisticsUseCase: GetWaterStatisticsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        loadChartData()
        loadWeeklyProgress()
        loadStatistics()
    }

    private fun loadChartData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val currentState = _state.value
            if (currentState.selectedTab == 0) {
                // Monthly data
                getMonthlyDataUseCase(
                    currentState.currentDate.year,
                    currentState.currentDate.monthValue
                ).collect { data ->
                    _state.update { it.copy(chartData = data, isLoading = false) }
                }
            } else {
                // Yearly data
                getYearlyDataUseCase(currentState.currentDate.year).collect { data ->
                    _state.update { it.copy(chartData = data, isLoading = false) }
                }
            }
        }
    }

    private fun loadWeeklyProgress() {
        viewModelScope.launch {
            getWeeklyProgressUseCase().collect { progress ->
                _state.update { it.copy(weeklyProgress = progress) }
            }
        }
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            getWaterStatisticsUseCase().collect { stats ->
                _state.update { it.copy(statistics = stats) }
            }
        }
    }

    fun changeTab(tab: Int) {
        _state.update { it.copy(selectedTab = tab) }
        loadChartData()
    }

    fun navigateToPrevious() {
        val currentState = _state.value
        val newDate = if (currentState.selectedTab == 0) {
            currentState.currentDate.minusMonths(1)
        } else {
            currentState.currentDate.minusYears(1)
        }
        _state.update { it.copy(currentDate = newDate) }
        loadChartData()
    }

    fun navigateToNext() {
        val currentState = _state.value
        val newDate = if (currentState.selectedTab == 0) {
            currentState.currentDate.plusMonths(1)
        } else {
            currentState.currentDate.plusYears(1)
        }
        _state.update { it.copy(currentDate = newDate) }
        loadChartData()
    }
}