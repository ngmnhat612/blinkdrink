// ============================================================================
// FILE: presentation/dashboard/DashboardViewModel.kt
// ============================================================================
package com.example.blinkdrink.presentation.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkdrink.domain.model.HistoryRecord
import com.example.blinkdrink.domain.usecase.history.*
import com.example.blinkdrink.domain.usecase.settings.*
import com.example.blinkdrink.domain.usecase.weather.GetCurrentWeatherUseCase
import com.example.blinkdrink.domain.usecase.weather.ObserveWeatherUpdatesUseCase
import com.example.blinkdrink.domain.repository.SettingsRepository
import com.example.blinkdrink.util.EyeBreakScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val observeWeatherUpdatesUseCase: ObserveWeatherUpdatesUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getWaterGoalUseCase: GetWaterGoalUseCase,
    private val getSelectedWaterCupSizeUseCase: GetSelectedWaterCupSizeUseCase,
    private val updateSelectedWaterCupSizeUseCase: UpdateSelectedWaterCupSizeUseCase,
    private val getDailyWaterIntakeUseCase: GetDailyWaterIntakeUseCase,
    private val updateDailyWaterIntakeUseCase: UpdateDailyWaterIntakeUseCase,
    private val getDailyEyeBreakCountUseCase: GetDailyEyeBreakCountUseCase,
    private val updateDailyEyeBreakCountUseCase: UpdateDailyEyeBreakCountUseCase,
    private val checkAndResetDailyDataUseCase: CheckAndResetDailyDataUseCase,
    private val getAdjustedWaterGoalUseCase: GetAdjustedWaterGoalUseCase,
    private val adjustWaterGoalByWeatherUseCase: AdjustWaterGoalByWeatherUseCase,
    private val getHasAdjustedTodayUseCase: GetHasAdjustedTodayUseCase,
    private val getEyeBreakFrequencyUseCase: GetEyeBreakFrequencyUseCase,
    private val getEyeBreakDurationUseCase: GetEyeBreakDurationUseCase,
    private val getEyeBreakSessionActiveUseCase: GetEyeBreakSessionActiveUseCase,
    private val setEyeBreakSessionActiveUseCase: SetEyeBreakSessionActiveUseCase,
    private val getEyeBreakSessionDurationUseCase: GetEyeBreakSessionDurationUseCase,
    private val setEyeBreakSessionDurationUseCase: SetEyeBreakSessionDurationUseCase,
    private val getEyeBreakSessionStartTimeUseCase: GetEyeBreakSessionStartTimeUseCase,
    private val setEyeBreakSessionStartTimeUseCase: SetEyeBreakSessionStartTimeUseCase,
    private val getEyeBreakOnBreakUseCase: GetEyeBreakOnBreakUseCase,
    private val setEyeBreakOnBreakUseCase: SetEyeBreakOnBreakUseCase,
    private val getEyeBreakBreakStartTimeUseCase: GetEyeBreakBreakStartTimeUseCase,
    private val setEyeBreakBreakStartTimeUseCase: SetEyeBreakBreakStartTimeUseCase,
    private val getTodayHistoryUseCase: GetTodayHistoryUseCase,
    private val addHistoryRecordUseCase: AddHistoryRecordUseCase,
    private val removeHistoryRecordUseCase: RemoveHistoryRecordUseCase,
    private val clearTodayHistoryUseCase: ClearTodayHistoryUseCase,
    private val settingsRepository: SettingsRepository // ⭐ THÊM - Để undo điều chỉnh thời tiết
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private var eyeBreakTimerJob: Job? = null
    private var sessionTimerJob: Job? = null

    init {
        initializeData()
        restoreEyeBreakSession()
        loadTodayHistory()
    }

    private fun initializeData() {
        observeWeatherUpdates()
        loadBaseWaterGoal()
        loadAdjustedWaterGoal()
        loadSelectedCupSize()
        loadDailyWaterIntake()
        loadDailyEyeBreakCount()
        loadHasAdjustedFlag()
        loadEyeBreakSettings()
    }

    private fun loadEyeBreakSettings() {
        viewModelScope.launch {
            getEyeBreakFrequencyUseCase().collect { frequency ->
                _state.update { it.copy(eyeBreakFrequency = frequency) }
            }
        }

        viewModelScope.launch {
            getEyeBreakDurationUseCase().collect { duration ->
                _state.update { it.copy(eyeBreakDurationMinutes = duration) }
            }
        }
    }

    // ✅ Load history từ DataStore
    private fun loadTodayHistory() {
        viewModelScope.launch {
            getTodayHistoryUseCase().collect { records ->
                _state.update { it.copy(historyList = records) }
            }
        }
    }

    // ========================================================================
    // EYE BREAK SESSION METHODS
    // ========================================================================

    private fun restoreEyeBreakSession() {
        viewModelScope.launch {
            val isActive = getEyeBreakSessionActiveUseCase().first()

            if (isActive) {
                val sessionDuration = getEyeBreakSessionDurationUseCase().first()
                val sessionStartTime = getEyeBreakSessionStartTimeUseCase().first()
                val isOnBreak = getEyeBreakOnBreakUseCase().first()
                val breakStartTime = getEyeBreakBreakStartTimeUseCase().first()
                val eyeBreakDuration = getEyeBreakDurationUseCase().first()

                val currentTime = System.currentTimeMillis()
                val sessionElapsedSeconds = ((currentTime - sessionStartTime) / 1000).toInt()
                val sessionTotalSeconds = sessionDuration * 60
                val sessionRemainingSeconds = (sessionTotalSeconds - sessionElapsedSeconds).coerceAtLeast(0)

                _state.update {
                    it.copy(
                        isEyeBreakSessionActive = true,
                        eyeBreakSessionDurationMinutes = sessionDuration,
                        sessionRemainingSeconds = sessionRemainingSeconds,
                        isOnEyeBreak = isOnBreak
                    )
                }

                if (sessionRemainingSeconds > 0) {
                    startSessionTimer()
                } else {
                    finishSession()
                    return@launch
                }

                if (isOnBreak && breakStartTime > 0) {
                    val breakElapsedSeconds = ((currentTime - breakStartTime) / 1000).toInt()
                    val totalBreakSeconds = eyeBreakDuration * 60
                    val remainingSeconds = (totalBreakSeconds - breakElapsedSeconds).coerceAtLeast(0)

                    _state.update {
                        it.copy(eyeBreakRemainingSeconds = remainingSeconds)
                    }

                    if (remainingSeconds > 0) {
                        startBreakCountdownTimer()
                    } else {
                        finishEyeBreak()
                    }
                }
            }
        }
    }

    private fun startSessionTimer() {
        sessionTimerJob?.cancel()
        sessionTimerJob = viewModelScope.launch {
            while (_state.value.sessionRemainingSeconds > 0) {
                delay(1000L)

                _state.update {
                    it.copy(sessionRemainingSeconds = it.sessionRemainingSeconds - 1)
                }

                if (_state.value.sessionRemainingSeconds <= 0) {
                    finishSession()
                }
            }
        }
    }

    private fun finishSession() {
        sessionTimerJob?.cancel()
        eyeBreakTimerJob?.cancel()

        viewModelScope.launch {
            setEyeBreakSessionActiveUseCase(false)
            setEyeBreakSessionDurationUseCase(0)
            setEyeBreakSessionStartTimeUseCase(0L)
            setEyeBreakOnBreakUseCase(false)
            setEyeBreakBreakStartTimeUseCase(0L)

            updateDailyEyeBreakCountUseCase(0)

            _state.update {
                it.copy(
                    isEyeBreakSessionActive = false,
                    eyeBreakSessionDurationMinutes = 0,
                    sessionRemainingSeconds = 0,
                    eyeBreakRemainingSeconds = 0,
                    isOnEyeBreak = false,
                    eyeRestCount = 0
                )
            }

            EyeBreakScheduler.cancelAllSchedules(context)
        }
    }

    private fun startBreakCountdownTimer() {
        eyeBreakTimerJob?.cancel()
        eyeBreakTimerJob = viewModelScope.launch {
            while (_state.value.eyeBreakRemainingSeconds > 0) {
                delay(1000L)

                _state.update {
                    it.copy(eyeBreakRemainingSeconds = it.eyeBreakRemainingSeconds - 1)
                }

                if (_state.value.eyeBreakRemainingSeconds <= 0) {
                    finishEyeBreak()
                }
            }
        }
    }

    fun startEyeBreakSession(durationMinutes: Int) {
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()

            setEyeBreakSessionActiveUseCase(true)
            setEyeBreakSessionDurationUseCase(durationMinutes)
            setEyeBreakSessionStartTimeUseCase(startTime)
            setEyeBreakOnBreakUseCase(false)
            setEyeBreakBreakStartTimeUseCase(0L)

            _state.update {
                it.copy(
                    isEyeBreakSessionActive = true,
                    eyeBreakSessionDurationMinutes = durationMinutes,
                    sessionRemainingSeconds = durationMinutes * 60,
                    eyeBreakRemainingSeconds = 0,
                    isOnEyeBreak = false
                )
            }

            startSessionTimer()

            val breakDuration = _state.value.eyeBreakDurationMinutes
            val frequency = _state.value.eyeBreakFrequency

            // ✅ LẤY SỐ LẦN THỰC TẾ từ việc lên lịch
            val actualExpectedBreaks = EyeBreakScheduler.scheduleAllEyeBreakReminders(
                context,
                durationMinutes,
                breakDuration,
                frequency
            )

            // ✅ LƯU số lần expected THỰC TẾ vào DataStore
            settingsRepository.setExpectedBreakCount(actualExpectedBreaks)
        }
    }

    fun cancelEyeBreakSession() {
        finishSession()
    }

    fun confirmEyeBreak() {
        if (_state.value.isOnEyeBreak) return

        val currentState = _state.value
        val breakDurationMinutes = currentState.eyeBreakDurationMinutes

        viewModelScope.launch {
            val breakStartTime = System.currentTimeMillis()

            setEyeBreakOnBreakUseCase(true)
            setEyeBreakBreakStartTimeUseCase(breakStartTime)

            _state.update {
                it.copy(
                    isOnEyeBreak = true,
                    eyeBreakRemainingSeconds = breakDurationMinutes * 60
                )
            }

            startBreakCountdownTimer()

            EyeBreakScheduler.scheduleBreakFinished(context, breakDurationMinutes)
        }
    }

    private fun finishEyeBreak() {
        viewModelScope.launch {
            val currentState = _state.value
            val newCount = currentState.eyeRestCount + 1

            updateDailyEyeBreakCountUseCase(newCount)

            setEyeBreakOnBreakUseCase(false)
            setEyeBreakBreakStartTimeUseCase(0L)

            _state.update {
                it.copy(
                    eyeRestCount = newCount,
                    isOnEyeBreak = false,
                    eyeBreakRemainingSeconds = 0
                )
            }

            val expectedBreaks = calculateExpectedBreaks(currentState.eyeBreakSessionDurationMinutes)
            if (newCount >= expectedBreaks) {
                finishSession()
            } else {
                val breakInterval = calculateBreakInterval(currentState.eyeBreakSessionDurationMinutes)
                EyeBreakScheduler.scheduleBreakReminder(context, breakInterval)
            }
        }
    }

    fun calculateExpectedBreaks(sessionDurationMinutes: Int): Int {
        if (sessionDurationMinutes == 0) return 0

        val frequency = _state.value.eyeBreakFrequency
        val breakDuration = _state.value.eyeBreakDurationMinutes

        // ✅ SỬ DỤNG hàm tính THỰC TẾ từ Scheduler
        return EyeBreakScheduler.calculateActualExpectedBreaks(
            sessionDurationMinutes,
            breakDuration,
            frequency
        )
    }

    private fun calculateBreakInterval(sessionDurationMinutes: Int): Int {
        val frequency = _state.value.eyeBreakFrequency
        return (60 / frequency)
    }

    fun formatTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, secs)
        } else {
            String.format("%02d:%02d", minutes, secs)
        }
    }

    // ========================================================================
    // WATER INTAKE METHODS
    // ========================================================================

    private fun loadBaseWaterGoal() {
        viewModelScope.launch {
            getWaterGoalUseCase().collect { baseGoal ->
                _state.update { it.copy(baseWaterGoal = baseGoal) }
            }
        }
    }

    private fun loadAdjustedWaterGoal() {
        viewModelScope.launch {
            getAdjustedWaterGoalUseCase().collect { adjustedGoal ->
                val displayGoal = if (adjustedGoal == 0) {
                    _state.value.baseWaterGoal
                } else {
                    adjustedGoal
                }
                _state.update { it.copy(dailyWaterGoal = displayGoal) }
            }
        }
    }

    private fun loadSelectedCupSize() {
        viewModelScope.launch {
            getSelectedWaterCupSizeUseCase().collect { size ->
                _state.update { it.copy(selectedWaterAmount = size) }
            }
        }
    }

    private fun loadDailyWaterIntake() {
        viewModelScope.launch {
            getDailyWaterIntakeUseCase().collect { intake ->
                _state.update { it.copy(currentIntake = intake) }
            }
        }
    }

    fun updateWaterAmount(amount: Int) {
        viewModelScope.launch {
            updateSelectedWaterCupSizeUseCase(amount)
            _state.update { it.copy(selectedWaterAmount = amount) }
        }
    }

    fun confirmWaterIntake() {
        viewModelScope.launch {
            val currentState = _state.value
            val newIntake = currentState.currentIntake + currentState.selectedWaterAmount

            updateDailyWaterIntakeUseCase(newIntake)

            val newHistory = HistoryRecord(
                time = getCurrentTime(),
                type = "Uống nước",
                amount = "${currentState.selectedWaterAmount} ml",
                icon = "🥤"
            )

            addHistoryRecordUseCase(newHistory)

            _state.update {
                it.copy(currentIntake = newIntake)
            }
        }
    }

    private fun loadDailyEyeBreakCount() {
        viewModelScope.launch {
            getDailyEyeBreakCountUseCase().collect { count ->
                _state.update { it.copy(eyeRestCount = count) }
            }
        }
    }

    // ========================================================================
    // WEATHER METHODS
    // ========================================================================

    // Gọi API lấy thời tiết trên luồng background
    private fun observeWeatherUpdates() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingWeather = true) }

            observeWeatherUpdatesUseCase().collect { result ->
                result.fold(
                    onSuccess = { weatherInfo ->
                        _state.update {
                            it.copy(
                                weatherInfo = weatherInfo,
                                isLoadingWeather = false
                            )
                        }
                        checkAndResetDailyData(weatherInfo.waterAdjustment)
                    },
                    onFailure = { error ->
                        _state.update { it.copy(isLoadingWeather = false) }
                        checkAndResetDailyData(0)
                    }
                )
            }
        }
    }

    private fun checkAndResetDailyData(weatherAdjustment: Int) {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Date())

            val lastResetDate = settingsRepository.getLastResetDate().first()

            if (lastResetDate != today) {
                // Reset daily data
                checkAndResetDailyDataUseCase(weatherAdjustment)

                // ⭐ Xóa history của ngày cũ
                clearTodayHistoryUseCase()
            }
        }
    }

    private fun loadHasAdjustedFlag() {
        viewModelScope.launch {
            getHasAdjustedTodayUseCase().collect { hasAdjusted ->
                _state.update { it.copy(hasAdjustedToday = hasAdjusted) }
            }
        }
    }

    fun adjustWaterGoalByWeather(adjustment: Int) {
        viewModelScope.launch {
            val success = adjustWaterGoalByWeatherUseCase(adjustment)

            if (success) {
                val actionType = if (adjustment >= 0) "Tăng mục tiêu" else "Giảm mục tiêu"

                val newHistory = HistoryRecord(
                    time = getCurrentTime(),
                    type = "$actionType (Thời tiết)",
                    amount = "${kotlin.math.abs(adjustment)} ml",
                    icon = if (adjustment >= 0) "🌡️" else "❄️"
                )

                addHistoryRecordUseCase(newHistory)
            }
        }
    }

    fun refreshWeather() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingWeather = true) }

            val result = getCurrentWeatherUseCase()
            result.fold(
                onSuccess = { weatherInfo ->
                    _state.update {
                        it.copy(
                            weatherInfo = weatherInfo,
                            isLoadingWeather = false
                        )
                    }
                },
                onFailure = { error ->
                    _state.update { it.copy(isLoadingWeather = false) }
                }
            )
        }
    }

    // ========================================================================
    // HISTORY METHODS
    // ========================================================================

    fun undoHistoryRecord(recordId: String) {
        viewModelScope.launch {
            val record = _state.value.historyList.find { it.id == recordId }

            if (record != null) {
                // Xóa khỏi history
                removeHistoryRecordUseCase(recordId)

                // Cập nhật lại dữ liệu tương ứng
                when {
                    record.type == "Uống nước" -> {
                        // Trừ lại lượng nước đã uống
                        val amount = record.amount.replace(" ml", "").toIntOrNull() ?: 0
                        val newIntake = (_state.value.currentIntake - amount).coerceAtLeast(0)
                        updateDailyWaterIntakeUseCase(newIntake)

                        _state.update { it.copy(currentIntake = newIntake) }
                    }

                    record.type.contains("mục tiêu") && record.type.contains("Thời tiết") -> {
                        // Reset lại flag điều chỉnh
                        settingsRepository.setHasAdjustedToday(false)

                        // Đặt lại mục tiêu về baseGoal
                        val baseGoal = _state.value.baseWaterGoal
                        settingsRepository.setAdjustedDailyWaterGoal(baseGoal)

                        _state.update {
                            it.copy(
                                dailyWaterGoal = baseGoal,
                                hasAdjustedToday = false
                            )
                        }
                    }
                }
            }
        }
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================

    private fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    }

    override fun onCleared() {
        super.onCleared()
        eyeBreakTimerJob?.cancel()
        sessionTimerJob?.cancel()
    }
}