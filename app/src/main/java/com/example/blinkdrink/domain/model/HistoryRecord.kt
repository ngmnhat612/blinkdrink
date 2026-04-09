// ============================================================================
// FILE: domain/model/HistoryRecord.kt
// ============================================================================
package com.example.blinkdrink.domain.model

data class HistoryRecord(
    val id: String = java.util.UUID.randomUUID().toString(),
    val time: String,
    val type: String,
    val amount: String,
    val icon: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class DayProgress(
    val label: String,     // "T2", "T3", ...
    val day: Int,          // Ngày trong tháng
    val intake: Int,       // Lượng nước đã uống (ml)
    val goal: Int,         // Mục tiêu (ml)
    val completed: Boolean // Đã hoàn thành?
)

data class WaterStatistics(
    val weeklyAverage: Int,      // Trung bình tuần (ml)
    val monthlyAverage: Int,     // Trung bình tháng (ml)
    val completionRate: Int,     // Tỷ lệ hoàn thành (%)
    val drinkingFrequency: Int   // Tần suất uống (lần/ngày)
)