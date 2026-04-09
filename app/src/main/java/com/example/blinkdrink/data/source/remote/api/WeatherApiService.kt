// ============================================================================
// FILE: data/source/remote/api/WeatherApiService.kt
// ============================================================================
package com.example.blinkdrink.data.source.remote.api

import com.example.blinkdrink.data.model.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    // Open-Meteo API - MIỄN PHÍ, KHÔNG CẦN API KEY!
    // Định nghĩa request GET
    @GET("forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,weather_code",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min",
        @Query("timezone") timezone: String = "Asia/Ho_Chi_Minh",
        @Query("forecast_days") forecastDays: Int = 1
    ): WeatherDto
}
