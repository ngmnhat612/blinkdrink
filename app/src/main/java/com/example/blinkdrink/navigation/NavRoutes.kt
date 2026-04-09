// ============================================================================
// FILE: navigation/NavRoutes.kt
// ============================================================================
package com.example.blinkdrink.navigation

sealed class NavRoutes(val route: String) {
    object Onboarding : NavRoutes("onboarding")
    object Dashboard : NavRoutes("dashboard")
    object Tips : NavRoutes("tips")
    object History : NavRoutes("history")
    object Settings : NavRoutes("settings")
}