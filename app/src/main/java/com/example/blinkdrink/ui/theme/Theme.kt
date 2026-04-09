// ============================================================================
// FILE: ui/theme/Theme.kt
// ============================================================================
package com.example.blinkdrink.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ============================================================================
// COMPOSITION LOCAL for Extended Colors
// ============================================================================

/**
 * CompositionLocal để truy cập các màu mở rộng trong Composable
 * Sử dụng: val extendedColors = LocalExtendedColors.current
 */
val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

// ============================================================================
// COLOR SCHEMES
// ============================================================================

// Màu sắc Light Theme (khớp với colors.xml)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006C4C),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF7DFEC1),
    onPrimaryContainer = Color(0xFF002114),
    secondary = Color(0xFF4C6356),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCEE9D7),
    onSecondaryContainer = Color(0xFF092016),
    tertiary = Color(0xFF3D6373),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC1E8FB),
    onTertiaryContainer = Color(0xFF001F29),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFBFDF9),
    onBackground = Color(0xFF191C1A),
    surface = Color(0xFFFBFDF9),
    onSurface = Color(0xFF191C1A),
    surfaceVariant = Color(0xFFDBE5DD),
    onSurfaceVariant = Color(0xFF404943),
    outline = Color(0xFF707973),
    inverseOnSurface = Color(0xFFEFF1ED),
    inverseSurface = Color(0xFF2E312F),
    inversePrimary = Color(0xFF60E0A6),
)

// Màu sắc Dark Theme (khớp với colors.xml)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF60E0A6),
    onPrimary = Color(0xFF003826),
    primaryContainer = Color(0xFF005139),
    onPrimaryContainer = Color(0xFF7DFEC1),
    secondary = Color(0xFFB2CDBB),
    onSecondary = Color(0xFF1E352A),
    secondaryContainer = Color(0xFF354B40),
    onSecondaryContainer = Color(0xFFCEE9D7),
    tertiary = Color(0xFFA5CCDF),
    onTertiary = Color(0xFF073543),
    tertiaryContainer = Color(0xFF254B5B),
    onTertiaryContainer = Color(0xFFC1E8FB),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF191C1A),
    onBackground = Color(0xFFE1E3DF),
    surface = Color(0xFF191C1A),
    onSurface = Color(0xFFE1E3DF),
    surfaceVariant = Color(0xFF404943),
    onSurfaceVariant = Color(0xFFBFC9C2),
    outline = Color(0xFF8A938C),
    inverseOnSurface = Color(0xFF191C1A),
    inverseSurface = Color(0xFFE1E3DF),
    inversePrimary = Color(0xFF006C4C),
)

// ============================================================================
// THEME COMPOSABLE
// ============================================================================

@Composable
fun BlinkDrinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Chọn Extended Colors theo theme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    // Cung cấp Extended Colors cho toàn bộ app
    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

// ============================================================================
// EXTENSION PROPERTIES for easier access
// ============================================================================

/**
 * Extension properties để truy cập màu dễ dàng hơn
 * Sử dụng: MaterialTheme.colorScheme.waterPrimary
 */
val androidx.compose.material3.ColorScheme.waterPrimary: Color
    @Composable
    get() = LocalExtendedColors.current.waterPrimary

val androidx.compose.material3.ColorScheme.waterBackground: Color
    @Composable
    get() = LocalExtendedColors.current.waterBackground

val androidx.compose.material3.ColorScheme.waterSurface: Color
    @Composable
    get() = LocalExtendedColors.current.waterSurface

val androidx.compose.material3.ColorScheme.eyeBreakPrimary: Color
    @Composable
    get() = LocalExtendedColors.current.eyeBreakPrimary

val androidx.compose.material3.ColorScheme.eyeBreakBackground: Color
    @Composable
    get() = LocalExtendedColors.current.eyeBreakBackground

val androidx.compose.material3.ColorScheme.eyeBreakSurface: Color
    @Composable
    get() = LocalExtendedColors.current.eyeBreakSurface

val androidx.compose.material3.ColorScheme.neutralBackground: Color
    @Composable
    get() = LocalExtendedColors.current.neutralBackground

val androidx.compose.material3.ColorScheme.neutralSurface: Color
    @Composable
    get() = LocalExtendedColors.current.neutralSurface

val androidx.compose.material3.ColorScheme.textPrimary: Color
    @Composable
    get() = LocalExtendedColors.current.textPrimary

val androidx.compose.material3.ColorScheme.textSecondary: Color
    @Composable
    get() = LocalExtendedColors.current.textSecondary

val androidx.compose.material3.ColorScheme.textTertiary: Color
    @Composable
    get() = LocalExtendedColors.current.textTertiary

val androidx.compose.material3.ColorScheme.waterOnPrimary: Color
    @Composable
    get() = LocalExtendedColors.current.waterOnPrimary

val androidx.compose.material3.ColorScheme.waterOnBackground: Color
    @Composable
    get() = LocalExtendedColors.current.waterOnBackground

val androidx.compose.material3.ColorScheme.eyeBreakOnPrimary: Color
    @Composable
    get() = LocalExtendedColors.current.eyeBreakOnPrimary

val androidx.compose.material3.ColorScheme.eyeBreakOnBackground: Color
    @Composable
    get() = LocalExtendedColors.current.eyeBreakOnBackground