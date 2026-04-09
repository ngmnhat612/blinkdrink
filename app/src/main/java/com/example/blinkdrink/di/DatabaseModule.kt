// ============================================================================
// FILE: di/DatabaseModule.kt
// ============================================================================
package com.example.blinkdrink.di

import android.content.Context
import androidx.room.Room
import com.example.blinkdrink.data.source.local.database.AppDatabase
import com.example.blinkdrink.data.source.local.database.dao.WaterLogDao
import com.example.blinkdrink.data.source.local.database.dao.EyeBreakLogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "blinkdrink_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideWaterLogDao(database: AppDatabase): WaterLogDao {
        return database.waterLogDao()
    }

    @Provides
    @Singleton
    fun provideEyeBreakLogDao(database: AppDatabase): EyeBreakLogDao {
        return database.eyeBreakLogDao()
    }
}