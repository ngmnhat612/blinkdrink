// ============================================================================
// FILE: presentation/settings/SettingsScreen.kt
// ============================================================================
package com.example.blinkdrink.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.blinkdrink.presentation.components.BottomNavigationBar
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var showGoalDialog by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }
    var showWakeTimeDialog by remember { mutableStateOf(false) }
    var showBedTimeDialog by remember { mutableStateOf(false) }
    var showEyeBreakDurationDialog by remember { mutableStateOf(false) } // ⭐ THÊM

    // ========================================================================
    // DIALOGS
    // ========================================================================
    if (showGoalDialog) {
        WaterGoalAdjustmentDialog(
            currentGoal = state.waterGoal,
            recommendedGoal = 2530,
            onDismiss = { showGoalDialog = false },
            onConfirm = { newGoal ->
                viewModel.updateWaterGoal(newGoal)
                showGoalDialog = false
            }
        )
    }

    if (showGenderDialog) {
        GenderSelectionDialog(
            currentGender = state.gender,
            onDismiss = { showGenderDialog = false },
            onConfirm = { newGender ->
                viewModel.updateGender(newGender)
                showGenderDialog = false
            }
        )
    }

    if (showWeightDialog) {
        WeightSelectionDialog(
            currentWeight = state.weight,
            onDismiss = { showWeightDialog = false },
            onConfirm = { newWeight ->
                viewModel.updateWeight(newWeight)
                showWeightDialog = false
            }
        )
    }

    if (showWakeTimeDialog) {
        TimeSelectionDialog(
            title = "Giờ thức dậy",
            currentTime = state.wakeTime,
            onDismiss = { showWakeTimeDialog = false },
            onConfirm = { newTime ->
                viewModel.updateWakeTime(newTime)
                showWakeTimeDialog = false
            }
        )
    }

    if (showBedTimeDialog) {
        TimeSelectionDialog(
            title = "Giờ đi ngủ",
            currentTime = state.bedTime,
            onDismiss = { showBedTimeDialog = false },
            onConfirm = { newTime ->
                viewModel.updateBedTime(newTime)
                showBedTimeDialog = false
            }
        )
    }

    // ⭐ DIALOG THỜI GIAN NGHỈ MẮT
    if (showEyeBreakDurationDialog) {
        EyeBreakDurationDialog(
            currentDuration = state.eyeBreakDuration,
            onDismiss = { showEyeBreakDurationDialog = false },
            onConfirm = { newDuration ->
                viewModel.updateEyeBreakDuration(newDuration)
                showEyeBreakDurationDialog = false
            }
        )
    }

    // ========================================================================
    // SCAFFOLD
    // ========================================================================
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Cài đặt", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ================================================================
            // CÀI ĐẶT NHẮC NHỞ
            // ================================================================
            SettingsSectionHeader("CÀI ĐẶT NHẮC NHỞ")

            SettingsItemRow(
                title = "Lịch nhắc nhở uống nước",
                showArrow = true,
                onClick = { navController.navigate("water_schedule") }
            )
            DividerLine()

            SettingsItemRow(
                title = "Lịch nhắc nghỉ mắt",
                valueText = "${state.eyeBreakFrequency} lần/giờ",
                showArrow = true,
                onClick = { navController.navigate("eye_break_schedule") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ================================================================
            // CHUNG
            // ================================================================
            SettingsSectionHeader("CHUNG")

            SettingsItemRow(title = "Đơn vị", valueText = "kg, ml")
            DividerLine()

            SettingsItemRow(
                title = "Mục tiêu lượng nước uống",
                valueText = "${state.waterGoal} ml",
                onClick = { showGoalDialog = true }
            )

            DividerLine()

            // ⭐ THỜI GIAN NGHỈ MẮT
            SettingsItemRow(
                title = "Thời gian nghỉ mắt",
                valueText = "${state.eyeBreakDuration} phút",
                onClick = { showEyeBreakDurationDialog = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ================================================================
            // DỮ LIỆU CÁ NHÂN
            // ================================================================
            SettingsSectionHeader("DỮ LIỆU CÁ NHÂN")

            SettingsItemRow(
                title = "Giới tính",
                valueText = state.gender,
                onClick = { showGenderDialog = true }
            )
            DividerLine()
            SettingsItemRow(
                title = "Cân nặng",
                valueText = "${state.weight} kg",
                onClick = { showWeightDialog = true }
            )
            DividerLine()
            SettingsItemRow(
                title = "Giờ thức dậy",
                valueText = state.wakeTime,
                onClick = { showWakeTimeDialog = true }
            )
            DividerLine()
            SettingsItemRow(
                title = "Giờ đi ngủ",
                valueText = state.bedTime,
                onClick = { showBedTimeDialog = true }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ============================================================================
// DIALOG COMPONENTS
// ============================================================================

@Composable
fun WaterGoalAdjustmentDialog(
    currentGoal: Int,
    recommendedGoal: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val minVal = 500f
    val maxVal = 15000f
    var sliderValue by remember { mutableFloatStateOf(currentGoal.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.shadow(12.dp, RoundedCornerShape(16.dp)),
        containerColor = Color.White,
        tonalElevation = 0.dp,
        title = {
            Text("Điều chỉnh mục tiêu", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${sliderValue.roundToInt()} ml", fontSize = 32.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { sliderValue = recommendedGoal.toFloat() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = minVal..maxVal,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color(0xFF42A5F5),
                        inactiveTrackColor = Color.LightGray.copy(alpha = 0.5f)
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(sliderValue.roundToInt()) }) {
                Text("ĐỒNG Ý", color = Color(0xFF42A5F5), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("HỦY BỎ", color = Color.Gray) }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

// ⭐ DIALOG THỜI GIAN NGHỈ MẮT (MỚI)
@Composable
fun EyeBreakDurationDialog(
    currentDuration: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var sliderValue by remember { mutableFloatStateOf(currentDuration.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("Thời gian nghỉ mắt", fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${sliderValue.roundToInt()} phút",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = 1f..10f,
                    steps = 8, // 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color(0xFF4CAF50),
                        inactiveTrackColor = Color.LightGray.copy(alpha = 0.5f)
                    )
                )
                Text(
                    text = "Khuyến nghị: 3-5 phút",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(sliderValue.roundToInt()) }) {
                Text("ĐỒNG Ý", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("HỦY BỎ", color = Color.Gray) }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun GenderSelectionDialog(
    currentGender: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val options = listOf("Nam", "Nữ")
    var selectedOption by remember { mutableStateOf(currentGender) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("Chọn giới tính", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedOption = option }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = { selectedOption = option },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF42A5F5))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option, fontSize = 16.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedOption) }) {
                Text("ĐỒNG Ý", color = Color(0xFF42A5F5), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("HỦY BỎ", color = Color.Gray) }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun WeightSelectionDialog(
    currentWeight: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var sliderValue by remember { mutableFloatStateOf(currentWeight.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("Cân nặng của bạn", fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${sliderValue.roundToInt()} kg",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF42A5F5),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = 30f..150f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color(0xFF42A5F5),
                        inactiveTrackColor = Color.LightGray.copy(alpha = 0.5f)
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(sliderValue.roundToInt()) }) {
                Text("ĐỒNG Ý", color = Color(0xFF42A5F5), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("HỦY BỎ", color = Color.Gray) }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelectionDialog(
    title: String,
    currentTime: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val parts = currentTime.split(":")
    val initialHour = parts.getOrNull(0)?.toIntOrNull() ?: 6
    val initialMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0

    val timeState = rememberTimePickerState(initialHour = initialHour, initialMinute = initialMinute, is24Hour = true)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TimePicker(
                    state = timeState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color(0xFFF0F0F0),
                        selectorColor = Color(0xFF42A5F5),
                        containerColor = Color.White,
                        periodSelectorBorderColor = Color(0xFF42A5F5),
                        timeSelectorSelectedContainerColor = Color(0xFF42A5F5).copy(alpha = 0.2f),
                        timeSelectorUnselectedContainerColor = Color(0xFFF0F0F0)
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val formattedTime = String.format("%02d:%02d", timeState.hour, timeState.minute)
                onConfirm(formattedTime)
            }) { Text("ĐỒNG Ý", color = Color(0xFF42A5F5), fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("HỦY BỎ", color = Color.Gray) }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

// ============================================================================
// HELPER COMPONENTS
// ============================================================================

@Composable
fun SettingsSectionHeader(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            fontSize = 13.sp
        ),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SettingsItemRow(
    title: String,
    valueText: String? = null,
    showArrow: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 16.sp, color = Color.Black)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (valueText != null) {
                Text(
                    text = valueText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF42A5F5)
                )
            }
            if (showArrow) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun DividerLine() {
    Divider(
        thickness = 0.5.dp,
        color = Color.LightGray.copy(alpha = 0.4f),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

fun Modifier.scale(scale: Float): Modifier = this.then(
    Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
)