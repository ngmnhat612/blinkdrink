// ============================================================================
// FILE: data/mapper/WeatherMapper.kt
// ============================================================================
package com.example.blinkdrink.data.mapper

import com.example.blinkdrink.data.model.dto.WeatherDto
import com.example.blinkdrink.domain.model.WeatherInfo

object WeatherMapper {

    fun toDomain(dto: WeatherDto, cityName: String): WeatherInfo {
        // Tính nhiệt độ trung bình từ min/max trong ngày
        val avgTemp = if (dto.daily.tempMax.isNotEmpty() && dto.daily.tempMin.isNotEmpty()) {
            ((dto.daily.tempMax[0] + dto.daily.tempMin[0]) / 2).toInt()
        } else {
            dto.current.temperature.toInt()
        }

        val condition = mapWeatherCode(dto.current.weatherCode)
        val icon = getWeatherIcon(dto.current.weatherCode)
        val waterAdjustment = calculateWaterAdjustment(avgTemp, dto.current.weatherCode)
        val tipsMessage = getWeatherTips(avgTemp, dto.current.weatherCode, waterAdjustment)

        return WeatherInfo(
            location = cityName,
            temperature = avgTemp,
            condition = condition,
            weatherIcon = icon,
            waterAdjustment = waterAdjustment,
            tipsMessage = tipsMessage
        )
    }

    // WMO Weather interpretation codes
    private fun mapWeatherCode(code: Int): String {
        return when (code) {
            0 -> "Quang đãng"
            1, 2, 3 -> "Có mây"
            45, 48 -> "Sương mù"
            51, 53, 55 -> "Mưa phùn"
            61, 63, 65 -> "Mưa"
            71, 73, 75 -> "Tuyết"
            77 -> "Tuyết hạt"
            80, 81, 82 -> "Mưa rào"
            85, 86 -> "Tuyết rào"
            95 -> "Dông"
            96, 99 -> "Dông có mưa đá"
            else -> "Khác"
        }
    }

    private fun getWeatherIcon(code: Int): String {
        return when (code) {
            0 -> "☀️"
            1, 2, 3 -> "☁️"
            45, 48 -> "🌫️"
            51, 53, 55 -> "🌦️"
            61, 63, 65 -> "🌧️"
            71, 73, 75, 77 -> "❄️"
            80, 81, 82 -> "🌧️"
            85, 86 -> "🌨️"
            95 -> "⛈️"
            96, 99 -> "⛈️"
            else -> "🌤️"
        }
    }

    private fun calculateWaterAdjustment(temperature: Int, weatherCode: Int): Int {
        // Base adjustment theo nhiệt độ
        val tempAdjustment = when {
            temperature >= 35 -> 700
            temperature >= 32 -> 500
            temperature >= 28 -> 300
            temperature >= 25 -> 200
            temperature >= 20 -> 0
            temperature >= 15 -> -100
            else -> -200
        }

        // Điều chỉnh thêm theo thời tiết
        val weatherAdjustment = when (weatherCode) {
            0 -> 100 // Nắng gắt → uống nhiều hơn
            61, 63, 65, 80, 81, 82 -> -50 // Mưa → uống ít hơn
            71, 73, 75, 77, 85, 86 -> -100 // Tuyết/lạnh → uống ít hơn
            else -> 0
        }

        return (tempAdjustment + weatherAdjustment).coerceIn(-300, 800)
    }

    private fun getWeatherTips(temperature: Int, weatherCode: Int, waterAdjustment: Int): String {
        val adjustmentText = when {
            waterAdjustment > 0 -> "khuyến nghị bổ sung thêm ${waterAdjustment}ml"
            waterAdjustment < 0 -> "có thể giảm ${Math.abs(waterAdjustment)}ml so với ngày thường"
            else -> "duy trì lượng nước tiêu chuẩn"
        }

        // Ưu tiên tips theo thời tiết đặc biệt
        return when (weatherCode) {
            61, 63, 65, 80, 81, 82 -> "Trời mưa, $adjustmentText"
            71, 73, 75, 77, 85, 86 -> "Trời lạnh, $adjustmentText"
            95, 96, 99 -> "Có dông, $adjustmentText"
            45, 48 -> "Sương mù, $adjustmentText"
            0 -> when {
                temperature >= 35 -> "Nắng gắt, $adjustmentText"
                temperature >= 30 -> "Trời nóng, $adjustmentText"
                temperature >= 25 -> "Thời tiết đẹp, $adjustmentText"
                else -> "Thời tiết mát, $adjustmentText"
            }
            else -> when {
                temperature >= 32 -> "Hôm nay trời nóng, $adjustmentText"
                temperature >= 25 -> "Nhiệt độ vừa phải, $adjustmentText"
                temperature >= 20 -> "Thời tiết mát, $adjustmentText"
                else -> "Trời se lạnh, $adjustmentText"
            }
        }
    }
}