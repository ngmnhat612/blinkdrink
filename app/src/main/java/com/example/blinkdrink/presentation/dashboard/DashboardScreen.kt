// ============================================================================
// FILE: presentation/dashboard/DashboardScreen.kt
// ============================================================================
package com.example.blinkdrink.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.blinkdrink.presentation.dashboard.components.*

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var showEyeBreakSessionDialog by remember { mutableStateOf(false) }
    var showEyeBreakManageDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        WeatherSection(
            weatherInfo = state.weatherInfo,
            isLoading = state.isLoadingWeather,
            hasAdjustedToday = state.hasAdjustedToday,
            onWaterAdjustmentClick = { adjustment ->
                viewModel.adjustWaterGoalByWeather(adjustment)
            }
        )

        WaterIntakeSection(
            currentIntake = state.currentIntake,
            dailyGoal = state.dailyWaterGoal,
            selectedAmount = state.selectedWaterAmount,
            onAmountChange = { viewModel.updateWaterAmount(it) },
            onConfirm = { viewModel.confirmWaterIntake() }
        )

        EyeBreakSection(
            currentCount = state.eyeRestCount,
            dailyGoal = state.dailyEyeGoal,
            isSessionActive = state.isEyeBreakSessionActive,
            sessionDurationMinutes = state.eyeBreakSessionDurationMinutes,
            sessionRemainingSeconds = state.sessionRemainingSeconds,
            isOnBreak = state.isOnEyeBreak,
            remainingSeconds = state.eyeBreakRemainingSeconds,
            eyeBreakFrequency = state.eyeBreakFrequency,
            expectedBreaks = viewModel.calculateExpectedBreaks(state.eyeBreakSessionDurationMinutes),
            onStartSession = { duration ->
                viewModel.startEyeBreakSession(duration)
                showEyeBreakSessionDialog = false
            },
            onCancelSession = {
                viewModel.cancelEyeBreakSession()
            },
            onConfirmBreak = {
                viewModel.confirmEyeBreak()
            },
            onClickIcon = {
                if (state.isEyeBreakSessionActive) {
                    showEyeBreakManageDialog = true
                } else {
                    showEyeBreakSessionDialog = true
                }
            },
            formatTime = { seconds -> viewModel.formatTime(seconds) }
        )

        // ⭐ THÊM onUndoClick callback
        TodayHistorySection(
            historyList = state.historyList,
            onUndoClick = { record ->
                viewModel.undoHistoryRecord(record.id)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showEyeBreakSessionDialog) {
        EyeBreakSessionDialog(
            eyeBreakFrequency = state.eyeBreakFrequency,
            onDismiss = { showEyeBreakSessionDialog = false },
            onStartSession = { duration ->
                viewModel.startEyeBreakSession(duration)
                showEyeBreakSessionDialog = false
            }
        )
    }

    if (showEyeBreakManageDialog) {
        EyeBreakManageSessionDialog(
            sessionDurationMinutes = state.eyeBreakSessionDurationMinutes,
            sessionRemainingSeconds = state.sessionRemainingSeconds,
            currentBreakCount = state.eyeRestCount,
            expectedBreaks = viewModel.calculateExpectedBreaks(state.eyeBreakSessionDurationMinutes),
            formatTime = { seconds -> viewModel.formatTime(seconds) },
            onDismiss = { showEyeBreakManageDialog = false },
            onResetSession = {
                viewModel.cancelEyeBreakSession()
                showEyeBreakManageDialog = false
                showEyeBreakSessionDialog = true
            },
            onCancelSession = {
                viewModel.cancelEyeBreakSession()
                showEyeBreakManageDialog = false
            }
        )
    }
}