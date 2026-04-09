// ============================================================================
// FILE: presentation/history/components/BarChart.kt
// ============================================================================
package com.example.blinkdrink.presentation.history.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blinkdrink.ui.theme.waterPrimary
import java.time.YearMonth

@Composable
fun BarChart(
    data: Map<Int, Int>,
    isMonthView: Boolean = true,
    currentMonth: Int = 1,
    currentYear: Int = 2024
) {
    // ✅ THAY ĐỔI: Dùng theme color
    val barColor = MaterialTheme.colorScheme.waterPrimary
    val gridColor = Color(0xFFEEEEEE)
    val yAxisLabelWidth = 28.dp

    val totalBars = if (isMonthView) {
        YearMonth.of(currentYear, currentMonth).lengthOfMonth()
    } else {
        12
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = "(%)",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val xStart = yAxisLabelWidth.toPx()
                val chartEffectiveWidth = width - xStart

                // GRID LINES + Y-AXIS LABELS
                val ySteps = 5
                for (i in 0 until ySteps) {
                    val yPos = height - (i * (height / (ySteps - 1)))

                    drawLine(
                        color = gridColor,
                        start = Offset(xStart, yPos),
                        end = Offset(width, yPos),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        (i * 25).toString(),
                        0f,
                        yPos + 10f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.LTGRAY
                            textSize = 32f
                            textAlign = android.graphics.Paint.Align.LEFT
                        }
                    )
                }

                // BARS
                val barSpacing = chartEffectiveWidth / totalBars
                val barWidth = (barSpacing * 0.6f).coerceAtMost(50.dp.toPx())
                val cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())

                data.forEach { (key, value) ->
                    val index = key - 1
                    val xCenter = xStart + (barSpacing * index) + (barSpacing / 2)
                    val barHeight = (value / 100f) * height

                    if (value > 0) {
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(xCenter - barWidth / 2, height - barHeight),
                            size = Size(barWidth, barHeight),
                            cornerRadius = cornerRadius
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        XAxisLabels(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = yAxisLabelWidth),
            isMonthView = isMonthView,
            totalBars = totalBars
        )
    }
}

@Composable
private fun XAxisLabels(
    modifier: Modifier = Modifier,
    isMonthView: Boolean,
    totalBars: Int
) {
    Box(modifier = modifier) {
        if (isMonthView) {
            MonthViewLabels(totalBars = totalBars)
        } else {
            YearViewLabels()
        }
    }
}

@Composable
private fun MonthViewLabels(totalBars: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val labelPositions = listOf(
            1,
            totalBars / 4,
            totalBars / 2,
            (totalBars * 3) / 4,
            totalBars
        )

        labelPositions.forEach { day ->
            Text(
                text = "$day",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun YearViewLabels() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("1", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.weight(3f))
        Text("4", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.weight(3f))
        Text("7", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.weight(3f))
        Text("10", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.weight(3f))
    }
}