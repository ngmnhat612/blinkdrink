// ============================================================================
// FILE: presentation/settings/SettingsViewModel.kt
// ============================================================================
package com.example.blinkdrink.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkdrink.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        loadAllSettings()
    }

    private fun loadAllSettings() {
        viewModelScope.launch {
            combine(
                settingsRepository.getEyeBreakFrequency(),
                settingsRepository.getEyeBreakDuration(), // ⭐ THÊM
                settingsRepository.getWaterGoal(),
                settingsRepository.getGender(),
                settingsRepository.getWeight(),
                settingsRepository.getWakeTime(),
                settingsRepository.getBedTime()
            ) { flows: Array<Any?> ->
                SettingsState(
                    eyeBreakFrequency = flows[0] as Int,
                    eyeBreakDuration = flows[1] as Int, // ⭐ THÊM
                    waterGoal = flows[2] as Int,
                    gender = flows[3] as String,
                    weight = flows[4] as Int,
                    wakeTime = flows[5] as String,
                    bedTime = flows[6] as String
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun updateWaterGoal(goal: Int) {
        viewModelScope.launch {
            settingsRepository.setWaterGoal(goal)
        }
    }

    // ⭐ THÊM - Update thời gian nghỉ mắt
    fun updateEyeBreakDuration(minutes: Int) {
        viewModelScope.launch {
            settingsRepository.setEyeBreakDuration(minutes)
        }
    }

    fun updateGender(gender: String) {
        viewModelScope.launch {
            settingsRepository.setGender(gender)
        }
    }

    fun updateWeight(weight: Int) {
        viewModelScope.launch {
            settingsRepository.setWeight(weight)
        }
    }

    fun updateWakeTime(time: String) {
        viewModelScope.launch {
            settingsRepository.setWakeTime(time)
            regenerateWaterSchedule() // ⭐ Tự động tạo lại lịch
        }
    }

    fun updateBedTime(time: String) {
        viewModelScope.launch {
            settingsRepository.setBedTime(time)
            regenerateWaterSchedule() // ⭐ Tự động tạo lại lịch
        }
    }

    // ⭐ THÊM - Tự động tạo lịch nhắc nước dựa trên giờ thức dậy & đi ngủ
    private fun regenerateWaterSchedule() {
        viewModelScope.launch {
            val wakeTime = _state.value.wakeTime
            val bedTime = _state.value.bedTime

            val schedule = generateWaterScheduleFromTimes(wakeTime, bedTime)
            settingsRepository.saveWaterSchedule(schedule)
        }
    }

    private fun generateWaterScheduleFromTimes(
        wakeTime: String,
        bedTime: String
    ): List<com.example.blinkdrink.domain.model.ReminderTime> {
        val wakeParts = wakeTime.split(":")
        val wakeHour = wakeParts[0].toIntOrNull() ?: 6
        val wakeMinute = wakeParts[1].toIntOrNull() ?: 0

        val bedParts = bedTime.split(":")
        val bedHour = bedParts[0].toIntOrNull() ?: 22
        val bedMinute = bedParts[1].toIntOrNull() ?: 0

        val wakeTimeInMinutes = wakeHour * 60 + wakeMinute
        val bedTimeInMinutes = bedHour * 60 + bedMinute

        // Tính khoảng cách giữa các mốc (chia đều trong ngày)
        val totalAwakeMinutes = if (bedTimeInMinutes > wakeTimeInMinutes) {
            bedTimeInMinutes - wakeTimeInMinutes
        } else {
            (24 * 60 - wakeTimeInMinutes) + bedTimeInMinutes
        }

        val numberOfReminders = 8 // 8 mốc nhắc nhở
        val intervalMinutes = totalAwakeMinutes / (numberOfReminders + 1)

        val reminders = mutableListOf<com.example.blinkdrink.domain.model.ReminderTime>()

        for (i in 1..numberOfReminders) {
            val reminderTimeInMinutes = wakeTimeInMinutes + (intervalMinutes * i)
            val hour = (reminderTimeInMinutes / 60) % 24
            val minute = reminderTimeInMinutes % 60

            reminders.add(
                com.example.blinkdrink.domain.model.ReminderTime(
                    id = i,
                    hour = hour,
                    minute = minute,
                    isEnabled = true
                )
            )
        }

        return reminders
    }
}