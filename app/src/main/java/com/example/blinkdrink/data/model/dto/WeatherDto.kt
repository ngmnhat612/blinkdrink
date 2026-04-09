// ============================================================================
// FILE: data/model/dto/WeatherDto.kt
// ============================================================================
package com.example.blinkdrink.data.model.dto

import com.google.gson.annotations.SerializedName

// Open-Meteo API Response (KHÔNG CẦN API KEY!)
data class WeatherDto(
    @SerializedName("current")
    val current: CurrentWeather,
    @SerializedName("daily")
    val daily: DailyWeather
)

data class CurrentWeather(
    @SerializedName("temperature_2m")
    val temperature: Double,
    @SerializedName("weather_code")
    val weatherCode: Int
)

data class DailyWeather(
    @SerializedName("temperature_2m_max")
    val tempMax: List<Double>,
    @SerializedName("temperature_2m_min")
    val tempMin: List<Double>
)