// ============================================================================
// FILE: presentation/tips/TipsState.kt
// ============================================================================
package com.example.blinkdrink.presentation.tips

// State đơn giản - có thể mở rộng sau này
data class TipsState(
    val isLoading: Boolean = false,
    val selectedCategory: TipsCategory = TipsCategory.ALL
)

enum class TipsCategory {
    ALL,
    WATER,
    EYE_EXERCISE
}