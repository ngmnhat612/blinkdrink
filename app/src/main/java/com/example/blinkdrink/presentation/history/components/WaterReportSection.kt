// ============================================================================
// FILE: presentation/history/components/WaterReportSection.kt
// ============================================================================
package com.example.blinkdrink.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blinkdrink.domain.model.WaterStatistics
import com.example.blinkdrink.ui.theme.waterPrimary

@Composable
fun WaterReportSection(statistics: WaterStatistics) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReportCard(
                modifier = Modifier.weight(1f),
                title = "Trung bình Tuần",
                value = "${statistics.weeklyAverage} ml",
                icon = Icons.Default.CalendarToday,
                accentColor = MaterialTheme.colorScheme.waterPrimary
            )
            ReportCard(
                modifier = Modifier.weight(1f),
                title = "Trung bình Tháng",
                value = "${statistics.monthlyAverage} ml",
                icon = Icons.Filled.InsertChartOutlined,
                accentColor = MaterialTheme.colorScheme.waterPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReportCard(
                modifier = Modifier.weight(1f),
                title = "Tần suất uống nước (Tháng)",
                value = "${statistics.drinkingFrequency} lần/ngày",
                icon = Icons.Default.Speed,
                accentColor = MaterialTheme.colorScheme.waterPrimary
            )
            ReportCard(
                modifier = Modifier.weight(1f),
                title = "Tỷ lệ hoàn thành mục tiêu ",
                value = "${statistics.completionRate}%",
                icon = Icons.Rounded.WaterDrop,
                accentColor = MaterialTheme.colorScheme.waterPrimary
            )
        }
    }
}

@Composable
private fun ReportCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(accentColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = title,
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}