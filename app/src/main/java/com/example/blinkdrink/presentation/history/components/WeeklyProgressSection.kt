// ============================================================================
// FILE: presentation/history/components/WeeklyProgressSection.kt
// ============================================================================
package com.example.blinkdrink.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blinkdrink.domain.model.DayProgress
import com.example.blinkdrink.ui.theme.waterPrimary

@Composable
fun WeeklyProgressSection(weekProgress: List<DayProgress>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        weekProgress.forEach { day ->
            DayVerticalBar(day)
        }
    }
}

@Composable
fun DayVerticalBar(day: DayProgress) {
    val percentage = if (day.goal > 0) (day.intake.toFloat() / day.goal).coerceIn(0f, 1f) else 0f
    val barHeight = 80.dp
    // ✅ THAY ĐỔI: Dùng theme color
    val activeColor = MaterialTheme.colorScheme.waterPrimary
    val inactiveColor = Color(0xFFE1E5EB)
    val completedTextColor = MaterialTheme.colorScheme.waterPrimary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(12.dp)
                .height(barHeight)
                .clip(RoundedCornerShape(50))
                .background(inactiveColor),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(percentage)
                    .background(activeColor)
            )
        }
        Text(
            text = day.label,
            fontSize = 12.sp,
            fontWeight = if (day.completed) FontWeight.Bold else FontWeight.Medium,
            color = if (day.completed) completedTextColor else Color.Gray
        )
    }
}