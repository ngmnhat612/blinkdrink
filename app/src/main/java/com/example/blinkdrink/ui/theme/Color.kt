// ============================================================================
// FILE: ui/theme/Color.kt
// ============================================================================
package com.example.blinkdrink.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// SEMANTIC COLORS - Feature-specific colors
// ============================================================================

/**
 * Màu sắc cho tính năng Water (Uống nước) - XANH BIỂN
 */
object WaterColors {
    // Light theme - XANH BIỂN ĐẬM
    val Primary = Color(0xFF0288D1)         // Xanh dương đậm chính
    val Background = Color(0xFFE1F5FE)      // Nền siêu nhạt
    val PrimaryVariant = Color(0xFF0277BD)  // Xanh dương đậm hơn
    val Surface = Color(0xFFB3E5FC)         // Xanh dương nhạt vừa
    val OnPrimary = Color(0xFFFFFFFF)       // Text trên nền Primary
    val OnBackground = Color(0xFF01579B)    // Text trên nền Background

    // Dark theme - XANH BIỂN SÁNG
    val PrimaryDark = Color(0xFF4FC3F7)     // Xanh dương sáng
    val BackgroundDark = Color(0xFF01579B)  // Nền xanh dương đậm
    val SurfaceDark = Color(0xFF0277BD)     // Surface xanh dương vừa
    val OnPrimaryDark = Color(0xFF01579B)   // Text trên nền Primary
    val OnBackgroundDark = Color(0xFFE1F5FE) // Text trên nền Background
}

/**
 * Màu sắc cho tính năng Eye Break (Nghỉ mắt) - XANH LÁ
 */
object EyeBreakColors {
    // Light theme - XANH LÁ ĐẬM
    val Primary = Color(0xFF388E3C)         // Xanh lá đậm chính
    val Background = Color(0xFFE8F5E9)      // Nền xanh lá nhạt
    val PrimaryVariant = Color(0xFF2E7D32)  // Xanh lá đậm hơn
    val Surface = Color(0xFFC8E6C9)         // Xanh lá nhạt vừa
    val OnPrimary = Color(0xFFFFFFFF)       // Text trên nền Primary
    val OnBackground = Color(0xFF1B5E20)    // Text trên nền Background

    // Dark theme - XANH LÁ SÁNG
    val PrimaryDark = Color(0xFF66BB6A)     // Xanh lá sáng
    val BackgroundDark = Color(0xFF1B5E20)  // Nền xanh lá đậm
    val SurfaceDark = Color(0xFF2E7D32)     // Surface xanh lá vừa
    val OnPrimaryDark = Color(0xFF1B5E20)   // Text trên nền Primary
    val OnBackgroundDark = Color(0xFFE8F5E9) // Text trên nền Background
}

/**
 * Màu sắc trung tính (Neutral) cho UI chung
 */
object NeutralColors {
    // Light theme
    val Background = Color(0xFFFAFAFA)      // Nền tổng thể xám khói nhạt
    val Surface = Color(0xFFFFFFFF)         // Nền trắng tinh
    val TextPrimary = Color(0xFF212121)     // Màu xám đen dịu mắt
    val TextSecondary = Color(0xFF424242)   // Màu xám đen nhạt hơn
    val TextTertiary = Color(0xFF757575)    // Màu xám
    val Divider = Color(0xFFE0E0E0)         // Đường phân cách

    // Dark theme
    val BackgroundDark = Color(0xFF121212)  // Nền tối
    val SurfaceDark = Color(0xFF1E1E1E)     // Surface tối
    val TextPrimaryDark = Color(0xFFE0E0E0) // Text sáng
    val TextSecondaryDark = Color(0xFFB0B0B0) // Text sáng nhạt
    val TextTertiaryDark = Color(0xFF808080)  // Text xám
    val DividerDark = Color(0xFF303030)     // Divider tối
}

// ============================================================================
// EXTENDED COLOR SCHEME - Thêm vào MaterialTheme
// ============================================================================

/**
 * Các màu mở rộng không có sẵn trong Material3 ColorScheme
 * Sử dụng với LocalExtendedColors.current trong Composable
 */
data class ExtendedColors(
    // Water colors
    val waterPrimary: Color,
    val waterBackground: Color,
    val waterSurface: Color,
    val waterOnPrimary: Color,
    val waterOnBackground: Color,

    // Eye Break colors
    val eyeBreakPrimary: Color,
    val eyeBreakBackground: Color,
    val eyeBreakSurface: Color,
    val eyeBreakOnPrimary: Color,
    val eyeBreakOnBackground: Color,

    // Neutral colors
    val neutralBackground: Color,
    val neutralSurface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
)

val LightExtendedColors = ExtendedColors(
    // Water
    waterPrimary = WaterColors.Primary,
    waterBackground = WaterColors.Background,
    waterSurface = WaterColors.Surface,
    waterOnPrimary = WaterColors.OnPrimary,
    waterOnBackground = WaterColors.OnBackground,

    // Eye Break
    eyeBreakPrimary = EyeBreakColors.Primary,
    eyeBreakBackground = EyeBreakColors.Background,
    eyeBreakSurface = EyeBreakColors.Surface,
    eyeBreakOnPrimary = EyeBreakColors.OnPrimary,
    eyeBreakOnBackground = EyeBreakColors.OnBackground,

    // Neutral
    neutralBackground = NeutralColors.Background,
    neutralSurface = NeutralColors.Surface,
    textPrimary = NeutralColors.TextPrimary,
    textSecondary = NeutralColors.TextSecondary,
    textTertiary = NeutralColors.TextTertiary,
)

val DarkExtendedColors = ExtendedColors(
    // Water
    waterPrimary = WaterColors.PrimaryDark,
    waterBackground = WaterColors.BackgroundDark,
    waterSurface = WaterColors.SurfaceDark,
    waterOnPrimary = WaterColors.OnPrimaryDark,
    waterOnBackground = WaterColors.OnBackgroundDark,

    // Eye Break
    eyeBreakPrimary = EyeBreakColors.PrimaryDark,
    eyeBreakBackground = EyeBreakColors.BackgroundDark,
    eyeBreakSurface = EyeBreakColors.SurfaceDark,
    eyeBreakOnPrimary = EyeBreakColors.OnPrimaryDark,
    eyeBreakOnBackground = EyeBreakColors.OnBackgroundDark,

    // Neutral
    neutralBackground = NeutralColors.BackgroundDark,
    neutralSurface = NeutralColors.SurfaceDark,
    textPrimary = NeutralColors.TextPrimaryDark,
    textSecondary = NeutralColors.TextSecondaryDark,
    textTertiary = NeutralColors.TextTertiaryDark,
)