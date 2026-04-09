// ============================================================================
// FILE: data/source/local/database/dao/EyeBreakLogDao.kt
// ============================================================================
package com.example.blinkdrink.data.source.local.database.dao

import androidx.room.*
import com.example.blinkdrink.data.model.entity.EyeBreakLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EyeBreakLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: EyeBreakLogEntity)

    @Query("SELECT * FROM eye_break_logs WHERE date = :date")
    fun getLogByDate(date: String): Flow<EyeBreakLogEntity?>

    @Query("SELECT COALESCE(SUM(count), 0) FROM eye_break_logs WHERE date >= :startDate AND date <= :endDate")
    suspend fun getTotalCountByDateRange(startDate: String, endDate: String): Int

    @Query("DELETE FROM eye_break_logs WHERE date = :date")
    suspend fun deleteByDate(date: String)

    @Query("DELETE FROM eye_break_logs")
    suspend fun deleteAll()
}