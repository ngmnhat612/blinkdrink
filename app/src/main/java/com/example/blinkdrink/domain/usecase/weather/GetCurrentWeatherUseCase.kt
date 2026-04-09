// ============================================================================
// FILE: domain/usecase/weather/GetCurrentWeatherUseCase.kt
// ============================================================================
package com.example.blinkdrink.domain.usecase.weather

import com.example.blinkdrink.domain.model.WeatherInfo
import com.example.blinkdrink.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): Result<WeatherInfo> {
        return weatherRepository.getCurrentWeather()
    }
}