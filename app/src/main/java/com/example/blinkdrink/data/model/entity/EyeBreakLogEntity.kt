// ============================================================================
// FILE: data/model/entity/EyeBreakLogEntity.kt
// ============================================================================
package com.example.blinkdrink.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eye_break_logs")
data class EyeBreakLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // "yyyy-MM-dd"
    val count: Int, // Số lần nghỉ trong ngày
    val timestamp: Long = System.currentTimeMillis()
)