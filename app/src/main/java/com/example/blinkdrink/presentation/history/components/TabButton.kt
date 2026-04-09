// ============================================================================
// FILE: presentation/history/components/TabButton.kt
// ============================================================================
package com.example.blinkdrink.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SegmentedControl(selectedTab: Int, onTabChange: (Int) -> Unit) {
    val backgroundColor = Color(0xFFF1F3F6)
    val indicatorColor = Color.White
    val selectedTextColor = Color.Black
    val unselectedTextColor = Color.Gray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selectedTab == 0) indicatorColor else Color.Transparent)
                    .clickable { onTabChange(0) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Theo Tháng",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selectedTab == 0) selectedTextColor else unselectedTextColor
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selectedTab == 1) indicatorColor else Color.Transparent)
                    .clickable { onTabChange(1) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Theo Năm",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selectedTab == 1) selectedTextColor else unselectedTextColor
                )
            }
        }
    }
}