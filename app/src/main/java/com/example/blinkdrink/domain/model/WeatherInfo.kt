// ============================================================================
// FILE: domain/model/WeatherInfo.kt
// ============================================================================
package com.example.blinkdrink.domain.model

data class WeatherInfo(
    val location: String = "Nha Trang, Khánh Hòa",
    val temperature: Int = 32,
    val condition: String = "Nắng nóng",
    val weatherIcon: String = "☀️",
    val waterAdjustment: Int = 500,
    val tipsMessage: String = "Hôm nay trời nóng, khuyến nghị bổ sung thêm 500ml"
)