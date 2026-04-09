// ============================================================================
// FILE: presentation/dashboard/components/WaterIntakeSection.kt
// ============================================================================
package com.example.blinkdrink.presentation.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blinkdrink.ui.theme.waterPrimary

@Composable
fun WaterIntakeSection(
    currentIntake: Int,
    dailyGoal: Int,
    selectedAmount: Int,
    onAmountChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = (currentIntake.toFloat() / dailyGoal.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    // ✅ THAY ĐỔI: Dùng theme color thay vì hard-coded
    val primaryColor = MaterialTheme.colorScheme.waterPrimary
    var showDialog by remember { mutableStateOf(false) }
    val waterOptions = listOf(50, 100, 150)

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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Nước uống", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(
                    "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = primaryColor
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Progress Circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp)
                    .clickable(
                        onClick = { showDialog = true },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            ) {
                Canvas(modifier = Modifier.size(150.dp)) {
                    val strokeW = 12.dp.toPx()
                    drawArc(Color(0xFFF0F2F5), -90f, 360f, false, style = Stroke(strokeW, cap = StrokeCap.Round))
                    drawArc(primaryColor, -90f, 360f * progress, false, style = Stroke(strokeW, cap = StrokeCap.Round))
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LocalDrink,
                        contentDescription = "Cốc nước",
                        tint = primaryColor,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("$selectedAmount ml", fontSize = 10.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("$currentIntake / $dailyGoal ml", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            // Button
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Uống ngay", fontWeight = FontWeight.Bold)
            }
        }
    }

    // Dialog
    if (showDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "Chọn loại cốc",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Bạn thường uống cốc loại nào?",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            waterOptions.forEach { amount ->
                                val isSelected = (selectedAmount == amount)
                                val color = if (isSelected) primaryColor else Color(0xFFE0E0E0)
                                val iconSize = when (amount) {
                                    50 -> 30.dp
                                    100 -> 40.dp
                                    else -> 55.dp
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clickable(
                                            onClick = { onAmountChange(amount) },
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        )
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.LocalDrink,
                                        contentDescription = null,
                                        tint = color,
                                        modifier = Modifier.size(iconSize)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "$amount ml",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) primaryColor else Color.Gray,
                                        fontSize = 14.sp
                                    )
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Canvas(modifier = Modifier.size(6.dp)) {
                                            drawCircle(color = primaryColor)
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Xong", fontWeight = FontWeight.Bold, color = primaryColor)
                }
            }
        )
    }
}