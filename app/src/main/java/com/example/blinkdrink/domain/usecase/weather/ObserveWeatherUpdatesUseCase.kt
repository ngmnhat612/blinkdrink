// ============================================================================
// FILE: domain/usecase/weather/ObserveWeatherUpdatesUseCase.kt
// ============================================================================
package com.example.blinkdrink.domain.usecase.weather

import com.example.blinkdrink.domain.model.WeatherInfo
import com.example.blinkdrink.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWeatherUpdatesUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(): Flow<Result<WeatherInfo>> {
        return weatherRepository.observeLocationChanges()
    }
}