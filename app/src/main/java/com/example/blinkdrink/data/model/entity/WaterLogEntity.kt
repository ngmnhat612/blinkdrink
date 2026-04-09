// ============================================================================
// FILE: data/model/entity/WaterLogEntity.kt
// ============================================================================
package com.example.blinkdrink.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Định nghĩa bảng dữ liệu (Entity)
@Entity(tableName = "water_logs")
data class WaterLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // "yyyy-MM-dd"
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis()
)