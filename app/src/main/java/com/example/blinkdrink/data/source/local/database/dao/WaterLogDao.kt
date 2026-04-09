// ============================================================================
// FILE: data/source/local/database/dao/WaterLogDao.kt
// ============================================================================
package com.example.blinkdrink.data.source.local.database.dao

import androidx.room.*
import com.example.blinkdrink.data.model.entity.WaterLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: WaterLogEntity)

    @Query("SELECT * FROM water_logs WHERE date = :date ORDER BY timestamp DESC")
    fun getLogsByDate(date: String): Flow<List<WaterLogEntity>>

    @Query("SELECT SUM(amountMl) FROM water_logs WHERE date = :date")
    suspend fun getTotalByDate(date: String): Int?

    @Query("SELECT SUM(amountMl) FROM water_logs WHERE date >= :startDate AND date <= :endDate")
    suspend fun getTotalByDateRange(startDate: String, endDate: String): Int?

    @Query("""
        SELECT date, SUM(amountMl) as totalMl
        FROM water_logs 
        WHERE date >= :startDate AND date <= :endDate
        GROUP BY date
        ORDER BY date
    """)
    suspend fun getGroupedByDate(startDate: String, endDate: String): List<DailyWaterSummary>

    @Query("SELECT COUNT(*) FROM water_logs WHERE date = :date")
    suspend fun getCountByDate(date: String): Int

    @Query("DELETE FROM water_logs WHERE date = :date")
    suspend fun deleteByDate(date: String)

    @Query("DELETE FROM water_logs")
    suspend fun deleteAll()
}

data class DailyWaterSummary(
    val date: String,
    val totalMl: Int
)