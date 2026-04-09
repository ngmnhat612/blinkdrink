// ============================================================================
// FILE: presentation/settings/components/EyeBreakScheduleViewModel.kt
// ============================================================================
package com.example.blinkdrink.presentation.settings.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkdrink.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EyeBreakScheduleViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EyeBreakScheduleState())
    val state: StateFlow<EyeBreakScheduleState> = _state.asStateFlow()

    init {
        loadFrequency()
    }

    private fun loadFrequency() {
        viewModelScope.launch {
            settingsRepository.getEyeBreakFrequency().collect { frequency ->
                _state.update { it.copy(selectedFrequency = frequency) }
            }
        }
    }

    fun updateFrequency(frequency: Int) {
        viewModelScope.launch {
            settingsRepository.setEyeBreakFrequency(frequency)
        }
    }
}

data class EyeBreakScheduleState(
    val selectedFrequency: Int = 2
)