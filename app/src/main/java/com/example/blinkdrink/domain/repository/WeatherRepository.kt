// ============================================================================
// FILE: domain/repository/WeatherRepository.kt
// ============================================================================
package com.example.blinkdrink.domain.repository

import com.example.blinkdrink.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(): Result<WeatherInfo>
    fun observeLocationChanges(): Flow<Result<WeatherInfo>>
}