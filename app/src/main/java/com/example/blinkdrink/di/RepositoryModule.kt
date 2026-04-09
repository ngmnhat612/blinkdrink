// ============================================================================
// FILE: di/RepositoryModule.kt
// ============================================================================
package com.example.blinkdrink.di

import com.example.blinkdrink.data.repository.HistoryRepositoryImpl
import com.example.blinkdrink.data.repository.SettingsRepositoryImpl
import com.example.blinkdrink.data.repository.UserRepositoryImpl
import com.example.blinkdrink.data.repository.WeatherRepositoryImpl
import com.example.blinkdrink.domain.repository.HistoryRepository
import com.example.blinkdrink.domain.repository.SettingsRepository
import com.example.blinkdrink.domain.repository.UserRepository
import com.example.blinkdrink.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository
}