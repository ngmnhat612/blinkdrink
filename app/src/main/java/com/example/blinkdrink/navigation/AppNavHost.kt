// ============================================================================
// FILE: navigation/AppNavHost.kt
// ============================================================================
package com.example.blinkdrink.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blinkdrink.presentation.components.BottomNavigationBar
import com.example.blinkdrink.presentation.dashboard.DashboardScreen
import com.example.blinkdrink.presentation.history.HistoryScreen
import com.example.blinkdrink.presentation.onboarding.OnboardingScreen
import com.example.blinkdrink.presentation.settings.SettingsScreen
import com.example.blinkdrink.presentation.settings.components.EyeBreakScheduleScreen
import com.example.blinkdrink.presentation.settings.components.WaterScheduleScreen
import com.example.blinkdrink.presentation.tips.TipsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    startWithOnboarding: Boolean,
    notificationAction: String? = null // ✅ THÊM PARAM
) {
    val navController = rememberNavController()
    val startDestination = if (startWithOnboarding) {
        NavRoutes.Onboarding.route
    } else {
        NavRoutes.Dashboard.route
    }

    // ✅ XỬ LÝ NOTIFICATION ACTION
    LaunchedEffect(notificationAction) {
        when (notificationAction) {
            "confirm_eye_break" -> {
                // Navigate to dashboard and show confirm dialog
                navController.navigate(NavRoutes.Dashboard.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                }
                // TODO: Trigger show confirm dialog in DashboardScreen
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Onboarding Screen
        composable(NavRoutes.Onboarding.route) {
            OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(NavRoutes.Dashboard.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Main App Screens
        composable(NavRoutes.Dashboard.route) {
            MainScreenWithBottomNav(navController) {
                DashboardScreen()
            }
        }

        composable(NavRoutes.Tips.route) {
            MainScreenWithBottomNav(navController) {
                TipsScreen()
            }
        }

        composable(NavRoutes.History.route) {
            HistoryScreen(navController = navController)
        }

        composable(NavRoutes.Settings.route) {
            SettingsScreen(navController = navController)
        }

        // Settings Sub-screens
        composable("eye_break_schedule") {
            EyeBreakScheduleScreen(navController = navController)
        }

        composable("water_schedule") {
            WaterScheduleScreen(navController = navController)
        }
    }
}

@Composable
private fun MainScreenWithBottomNav(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}