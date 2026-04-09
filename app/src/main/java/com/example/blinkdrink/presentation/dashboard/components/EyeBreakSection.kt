// ============================================================================
// FILE: presentation/dashboard/components/EyeBreakSection.kt
// ============================================================================
package com.example.blinkdrink.presentation.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blinkdrink.ui.theme.eyeBreakPrimary
import kotlin.math.roundToInt

@Composable
fun EyeBreakSection(
    currentCount: Int,
    dailyGoal: Int,
    isSessionActive: Boolean,
    sessionDurationMinutes: Int,
    sessionRemainingSeconds: Int,
    isOnBreak: Boolean,
    remainingSeconds: Int,
    eyeBreakFrequency: Int,
    expectedBreaks: Int,
    onStartSession: (Int) -> Unit,
    onCancelSession: () -> Unit,
    onConfirmBreak: () -> Unit,
    onClickIcon: () -> Unit,
    formatTime: (Int) -> String,
    modifier: Modifier = Modifier
) {
    val progress = if (isSessionActive && expectedBreaks > 0) {
        (currentCount.toFloat() / expectedBreaks.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    // ✅ THAY ĐỔI: Dùng theme color thay vì hard-coded
    val eyeColor = MaterialTheme.colorScheme.eyeBreakPrimary

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Nghỉ mắt", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(
                    "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = eyeColor
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp)
                    .clickable(
                        onClick = onClickIcon,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            ) {
                Canvas(modifier = Modifier.size(150.dp)) {
                    val strokeW = 12.dp.toPx()
                    drawArc(Color(0xFFF0F2F5), -90f, 360f, false, style = Stroke(strokeW, cap = StrokeCap.Round))
                    drawArc(eyeColor, -90f, 360f * progress, false, style = Stroke(strokeW, cap = StrokeCap.Round))
                }

                ModernEyeIcon(eyeColor = eyeColor, modifier = Modifier.size(40.dp, 28.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isSessionActive) {
                Text(
                    "Phiên: ${formatTime(sessionRemainingSeconds)}",
                    fontSize = 11.sp,
                    color = eyeColor,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = if (isSessionActive) {
                    "$currentCount / $expectedBreaks lần"
                } else {
                    "0 / 0 lần"
                },
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onConfirmBreak,
                enabled = isSessionActive && !isOnBreak,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = eyeColor,
                    disabledContainerColor = Color(0xFFE0E0E0)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                if (isOnBreak) {
                    val minutes = remainingSeconds / 60
                    val seconds = remainingSeconds % 60
                    Text(
                        String.format("Đang nghỉ... %02d:%02d", minutes, seconds),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                } else {
                    Text(
                        "Nghỉ mắt",
                        fontWeight = FontWeight.Bold,
                        color = if (isSessionActive) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun ModernEyeIcon(
    eyeColor: Color,
    modifier: Modifier = Modifier
) {
    val outlineColor = eyeColor
    val irisColor = eyeColor.copy(alpha = 0.4f)
    val pupilColor = eyeColor.copy(alpha = 0.8f)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2
        val cy = h / 2

        val eyePath = Path().apply {
            moveTo(0f, cy)
            quadraticBezierTo(cx, -h * 0.2f, w, cy)
            quadraticBezierTo(cx, h * 1.2f, 0f, cy)
            close()
        }

        drawPath(path = eyePath, color = Color.White)
        drawPath(
            path = eyePath,
            color = outlineColor,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        drawCircle(
            color = irisColor,
            radius = h * 0.28f,
            center = Offset(cx, cy)
        )

        drawCircle(
            color = pupilColor,
            radius = h * 0.14f,
            center = Offset(cx, cy)
        )

        drawCircle(
            color = Color.White,
            radius = h * 0.07f,
            center = Offset(cx + h * 0.08f, cy - h * 0.08f)
        )
    }
}

@Composable
fun EyeBreakSessionDialog(
    eyeBreakFrequency: Int,
    onDismiss: () -> Unit,
    onStartSession: (Int) -> Unit
) {
    var sessionDuration by remember { mutableFloatStateOf(120f) }
    // ✅ THAY ĐỔI: Dùng theme color
    val eyeColor = MaterialTheme.colorScheme.eyeBreakPrimary

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text("Phiên làm việc", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Bạn sẽ làm việc trong bao lâu?",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "${sessionDuration.roundToInt()} phút",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = eyeColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                val expectedBreaks = ((sessionDuration.roundToInt() / 60f) * eyeBreakFrequency).toInt()
                Text(
                    "Dự kiến: $expectedBreaks lần nghỉ mắt",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Slider(
                    value = sessionDuration,
                    onValueChange = { sessionDuration = it },
                    valueRange = 30f..480f,
                    steps = 29,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = eyeColor,
                        inactiveTrackColor = Color.LightGray.copy(alpha = 0.5f)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("30 phút", fontSize = 11.sp, color = Color.Gray)
                    Text("8 giờ", fontSize = 11.sp, color = Color.Gray)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val roundedDuration = (sessionDuration.roundToInt() / 15) * 15
                onStartSession(roundedDuration)
            }) {
                Text("BẮT ĐẦU", color = eyeColor, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("HỦY BỎ", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun EyeBreakManageSessionDialog(
    sessionDurationMinutes: Int,
    sessionRemainingSeconds: Int,
    currentBreakCount: Int,
    expectedBreaks: Int,
    formatTime: (Int) -> String,
    onDismiss: () -> Unit,
    onResetSession: () -> Unit,
    onCancelSession: () -> Unit
) {
    // ✅ THAY ĐỔI: Dùng theme color
    val eyeColor = MaterialTheme.colorScheme.eyeBreakPrimary

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text("Phiên làm việc", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = eyeColor.copy(alpha = 0.1f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Thời gian còn lại",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            formatTime(sessionRemainingSeconds),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = eyeColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Thời gian:", fontSize = 12.sp, color = Color.Gray)
                        Text(
                            "${sessionDurationMinutes} phút",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = eyeColor
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Tiến độ:", fontSize = 12.sp, color = Color.Gray)
                        Text(
                            "$currentBreakCount / $expectedBreaks lần",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = eyeColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onResetSession,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = eyeColor
                        )
                    ) {
                        Text("Điều chỉnh lại thời gian", fontWeight = FontWeight.Medium)
                    }

                    OutlinedButton(
                        onClick = onCancelSession,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFE53935)
                        )
                    ) {
                        Text("Kết thúc phiên", fontWeight = FontWeight.Medium)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("ĐÓNG", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}