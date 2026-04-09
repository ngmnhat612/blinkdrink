// ============================================================================
// FILE: presentation/onboarding/OnboardingViewModel.kt
// ============================================================================
package com.example.blinkdrink.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkdrink.domain.model.Gender
import com.example.blinkdrink.domain.usecase.user.SaveOnboardingDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveOnboardingDataUseCase: SaveOnboardingDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun onGenderChange(gender: Gender) {
        _state.update { it.copy(gender = gender) }
    }

    fun onWeightChange(weight: Int) {
        _state.update { it.copy(weightKg = weight.coerceIn(1, 300)) }
    }

    fun onWakeUpTimeChange(hour: Int, minute: Int) {
        _state.update {
            it.copy(
                wakeUpHour = hour.coerceIn(0, 23),
                wakeUpMinute = minute.coerceIn(0, 59)
            )
        }
    }

    fun onSleepTimeChange(hour: Int, minute: Int) {
        _state.update {
            it.copy(
                sleepHour = hour.coerceIn(0, 23),
                sleepMinute = minute.coerceIn(0, 59)
            )
        }
    }

    fun saveOnboardingData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val currentState = _state.value
                saveOnboardingDataUseCase(
                    gender = currentState.gender,
                    weightKg = currentState.weightKg,
                    wakeUpHour = currentState.wakeUpHour,
                    wakeUpMinute = currentState.wakeUpMinute,
                    sleepHour = currentState.sleepHour,
                    sleepMinute = currentState.sleepMinute
                )
                _state.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Không thể lưu dữ liệu. Vui lòng thử lại."
                    )
                }
            }
        }
    }
}