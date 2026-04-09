// ============================================================================
// FILE: presentation/settings/components/WaterScheduleViewModel.kt
// ============================================================================
package com.example.blinkdrink.presentation.settings.components

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkdrink.domain.model.ReminderTime
import com.example.blinkdrink.domain.repository.SettingsRepository
import com.example.blinkdrink.util.WaterReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaterScheduleViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WaterScheduleState())
    val state: StateFlow<WaterScheduleState> = _state.asStateFlow()

    init {
        loadSchedule()
    }

    private fun loadSchedule() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            settingsRepository.getWaterSchedule().collect { schedule ->
                _state.update {
                    it.copy(
                        reminders = schedule.sortedBy { reminder ->
                            reminder.hour * 60 + reminder.minute
                        },
                        isLoading = false
                    )
                }

                // Lên lịch lại khi load xong
                WaterReminderScheduler.scheduleAllWaterReminders(context, schedule)
            }
        }
    }

    fun addReminder(hour: Int, minute: Int) {
        viewModelScope.launch {
            val currentList = _state.value.reminders

            // Kiểm tra xem đã có nhắc nhở cùng giờ chưa
            val isDuplicate = currentList.any {
                it.hour == hour && it.minute == minute
            }

            if (isDuplicate) {
                // Có thể thêm thông báo cho user nếu muốn
                return@launch
            }

            val newId = generateNewId(currentList)

            val newReminder = ReminderTime(
                id = newId,
                hour = hour,
                minute = minute,
                isEnabled = true
            )

            val updatedList = (currentList + newReminder)
                .sortedBy { it.hour * 60 + it.minute }

            settingsRepository.saveWaterSchedule(updatedList)
            scheduleReminders(updatedList)
        }
    }

    fun toggleReminder(id: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            val updatedList = _state.value.reminders.map { reminder ->
                if (reminder.id == id) {
                    reminder.copy(isEnabled = isEnabled)
                } else {
                    reminder
                }
            }

            settingsRepository.saveWaterSchedule(updatedList)
            scheduleReminders(updatedList)
        }
    }

    fun deleteReminder(id: Int) {
        viewModelScope.launch {
            val updatedList = _state.value.reminders
                .filter { it.id != id }
                .sortedBy { it.hour * 60 + it.minute }

            settingsRepository.saveWaterSchedule(updatedList)
            scheduleReminders(updatedList)
        }
    }

    private fun scheduleReminders(reminders: List<ReminderTime>) {
        WaterReminderScheduler.scheduleAllWaterReminders(context, reminders)
    }

    private fun generateNewId(currentList: List<ReminderTime>): Int {
        return if (currentList.isEmpty()) {
            1
        } else {
            (currentList.maxOf { it.id }) + 1
        }
    }
}

data class WaterScheduleState(
    val reminders: List<ReminderTime> = emptyList(),
    val isLoading: Boolean = false
)