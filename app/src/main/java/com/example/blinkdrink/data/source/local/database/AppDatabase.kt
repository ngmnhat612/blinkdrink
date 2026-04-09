// ============================================================================
// FILE: data/source/local/database/AppDatabase.kt
// ============================================================================
package com.example.blinkdrink.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.blinkdrink.data.model.entity.WaterLogEntity
import com.example.blinkdrink.data.model.entity.EyeBreakLogEntity
import com.example.blinkdrink.data.source.local.database.dao.WaterLogDao
import com.example.blinkdrink.data.source.local.database.dao.EyeBreakLogDao

// Khởi tạo Database
@Database(
    entities = [
        WaterLogEntity::class,
        EyeBreakLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waterLogDao(): WaterLogDao
    abstract fun eyeBreakLogDao(): EyeBreakLogDao
}