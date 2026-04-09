// ============================================================================
// FILE: presentation/components/BottomNavigationBar.kt
// ============================================================================
package com.example.blinkdrink.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.blinkdrink.ui.theme.BlinkDrinkTheme
// ✅ THÊM IMPORTS CHO THEME COLORS
import com.example.blinkdrink.ui.theme.textPrimary
import com.example.blinkdrink.ui.theme.textTertiary
import com.example.blinkdrink.ui.theme.neutralBackground

// Định nghĩa cấu trúc cho 1 item trên Menu
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

// 1. Component thuần túy UI (Stateless) - Dễ dàng Preview và Test
@Composable
fun MenuBarContent(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("dashboard", "Trang chủ", Icons.Default.Home),
        BottomNavItem("tips", "Mẹo", Icons.Default.Lightbulb),
        BottomNavItem("history", "Lịch sử", Icons.Default.History),
        BottomNavItem("settings", "Cài đặt", Icons.Default.Settings)
    )

    NavigationBar(
        // ✅ THAY ĐỔI: Nền màu trắng
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        onNavigate(item.route)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    // ✅ SỬ DỤNG MÀU TỪ THEME
                    selectedIconColor = MaterialTheme.colorScheme.textPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.textPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.textTertiary,
                    unselectedTextColor = MaterialTheme.colorScheme.textTertiary,
                    indicatorColor = MaterialTheme.colorScheme.neutralBackground
                )
            )
        }
    }
}

// 2. Component Logic (Stateful) - Được sử dụng trong App thực tế
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    MenuBarContent(
        currentRoute = currentRoute,
        onNavigate = { route ->
            navController.navigate(route) {
                // Pop tất cả các màn hình khác để tránh back stack quá dài
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

// 3. Phần Preview
@Preview(showBackground = true, name = "Trang chủ đang chọn")
@Composable
fun MenuBarHomePreview() {
    BlinkDrinkTheme {
        MenuBarContent(currentRoute = "dashboard", onNavigate = {})
    }
}

@Preview(showBackground = true, name = "Cài đặt đang chọn")
@Composable
fun MenuBarSettingsPreview() {
    BlinkDrinkTheme {
        MenuBarContent(currentRoute = "settings", onNavigate = {})
    }
}

@Preview(
    showBackground = true,
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MenuBarDarkPreview() {
    BlinkDrinkTheme(darkTheme = true) {
        MenuBarContent(currentRoute = "dashboard", onNavigate = {})
    }
}