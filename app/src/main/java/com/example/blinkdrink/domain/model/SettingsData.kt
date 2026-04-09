// ============================================================================
// FILE: domain/model/SettingsData.kt
// ============================================================================
package com.example.blinkdrink.domain.model

data class ReminderTime(
    val id: Int,
    var hour: Int,
    var minute: Int,
    var isEnabled: Boolean
)

data class FrequencyOption(
    val frequencyPerHour: Int,
    val label: String,
    val subLabel: String
)