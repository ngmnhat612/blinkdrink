// ============================================================================
// FILE: data/source/remote/WeatherRemoteDataSource.kt
// ============================================================================
package com.example.blinkdrink.data.source.remote

import com.example.blinkdrink.data.model.dto.WeatherDto
import com.example.blinkdrink.data.source.remote.api.WeatherApiService
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherApiService: WeatherApiService
) {
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): Result<WeatherDto> {
        return try {
            val response = weatherApiService.getCurrentWeather(
                latitude = latitude,
                longitude = longitude
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}