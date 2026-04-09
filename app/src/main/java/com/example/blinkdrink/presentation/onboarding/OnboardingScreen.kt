// ============================================================================
// FILE: presentation/onboarding/OnboardingScreen.kt
// ============================================================================
package com.example.blinkdrink.presentation.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.blinkdrink.domain.model.Gender
import com.example.blinkdrink.ui.theme.BlinkDrinkTheme
// ✅ THÊM IMPORTS
import com.example.blinkdrink.ui.theme.waterPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var showWakeUpTimePicker by remember { mutableStateOf(false) }
    var showSleepTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onOnboardingComplete()
        }
    }

    OnboardingScreenContent(
        state = state,
        onGenderChange = { viewModel.onGenderChange(it) },
        onWeightChange = { viewModel.onWeightChange(it) },
        onWakeUpTimeClick = { showWakeUpTimePicker = true },
        onSleepTimeClick = { showSleepTimePicker = true },
        onSaveClick = { viewModel.saveOnboardingData() }
    )

    if (showWakeUpTimePicker) {
        TimePickerDialog(
            initialHour = state.wakeUpHour,
            initialMinute = state.wakeUpMinute,
            onConfirm = { hour, minute ->
                viewModel.onWakeUpTimeChange(hour, minute)
                showWakeUpTimePicker = false
            },
            onDismiss = { showWakeUpTimePicker = false }
        )
    }

    if (showSleepTimePicker) {
        TimePickerDialog(
            initialHour = state.sleepHour,
            initialMinute = state.sleepMinute,
            onConfirm = { hour, minute ->
                viewModel.onSleepTimeChange(hour, minute)
                showSleepTimePicker = false
            },
            onDismiss = { showSleepTimePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnboardingScreenContent(
    state: OnboardingState,
    onGenderChange: (Gender) -> Unit,
    onWeightChange: (Int) -> Unit,
    onWakeUpTimeClick: () -> Unit,
    onSleepTimeClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    // ✅ THÊM: Lấy màu từ theme
    val primaryColor = MaterialTheme.colorScheme.waterPrimary

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Hồ sơ của bạn",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = onSaveClick,
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                // ✅ THAY ĐỔI: Dùng theme color
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Bắt đầu", style = MaterialTheme.typography.titleMedium)
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Hãy cho chúng tôi biết một chút về bạn để cá nhân hóa trải nghiệm.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SectionLabel(text = "Giới tính")
            GenderSelectionRow(
                selectedGender = state.gender,
                onGenderChange = onGenderChange
            )

            SectionLabel(text = "Cân nặng (kg)")
            WeightSelectionControl(
                weight = state.weightKg,
                onWeightChange = onWeightChange
            )

            SectionLabel(text = "Thói quen ngủ")
            TimeSelectionCard(
                label = "Giờ thức dậy",
                timeValue = String.format("%02d:%02d", state.wakeUpHour, state.wakeUpMinute),
                icon = Icons.Default.WbSunny,
                onClick = onWakeUpTimeClick
            )
            TimeSelectionCard(
                label = "Giờ đi ngủ",
                timeValue = String.format("%02d:%02d", state.sleepHour, state.sleepMinute),
                icon = Icons.Default.DarkMode,
                onClick = onSleepTimeClick
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    // ✅ THAY ĐỔI: Dùng theme color
    val primaryColor = MaterialTheme.colorScheme.waterPrimary

    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = primaryColor,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun GenderSelectionRow(
    selectedGender: Gender,
    onGenderChange: (Gender) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GenderOptionCard(
            text = "Nam",
            icon = Icons.Default.Person,
            isSelected = selectedGender == Gender.MALE,
            onClick = { onGenderChange(Gender.MALE) },
            modifier = Modifier.weight(1f)
        )
        GenderOptionCard(
            text = "Nữ",
            icon = Icons.Default.Person,
            isSelected = selectedGender == Gender.FEMALE,
            onClick = { onGenderChange(Gender.FEMALE) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GenderOptionCard(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // ✅ THAY ĐỔI: Dùng theme color
    val primaryColor = MaterialTheme.colorScheme.waterPrimary

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) primaryColor.copy(alpha = 0.1f)
        else Color(0xFFF5F5F5),
        label = "color"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) primaryColor
        else Color(0xFF757575),
        label = "contentColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) primaryColor
        else Color.Transparent,
        label = "borderColor"
    )

    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, borderColor) else null
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor
            )
        }
    }
}

@Composable
private fun WeightSelectionControl(
    weight: Int,
    onWeightChange: (Int) -> Unit
) {
    // ✅ THAY ĐỔI: Dùng theme color
    val primaryColor = MaterialTheme.colorScheme.waterPrimary

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilledTonalIconButton(
                onClick = { if (weight > 1) onWeightChange(weight - 1) },
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = primaryColor.copy(alpha = 0.1f),
                    contentColor = primaryColor
                )
            ) {
                Icon(Icons.Default.HorizontalRule, contentDescription = "Giảm")
            }

            Text(
                text = "$weight",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = primaryColor
            )

            FilledTonalIconButton(
                onClick = { onWeightChange(weight + 1) },
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = primaryColor.copy(alpha = 0.1f),
                    contentColor = primaryColor
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tăng")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeSelectionCard(
    label: String,
    timeValue: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    // ✅ THAY ĐỔI: Dùng theme color
    val primaryColor = MaterialTheme.colorScheme.waterPrimary

    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(primaryColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = primaryColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Text(
                    text = timeValue,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Black
                )
            }

            Icon(
                Icons.Default.Edit,
                contentDescription = "Chọn giờ",
                tint = primaryColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    // ✅ THAY ĐỔI: Dùng theme color
    val primaryColor = MaterialTheme.colorScheme.waterPrimary

    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Chọn giờ",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color(0xFFF5F5F5),
                        selectorColor = primaryColor,
                        containerColor = Color.White,
                        periodSelectorBorderColor = primaryColor,
                        timeSelectorSelectedContainerColor = primaryColor.copy(alpha = 0.1f),
                        timeSelectorUnselectedContainerColor = Color(0xFFF5F5F5),
                        timeSelectorSelectedContentColor = primaryColor,
                        clockDialSelectedContentColor = Color.White
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            onConfirm(timePickerState.hour, timePickerState.minute)
                        }
                    ) {
                        Text("Xác nhận", color = primaryColor, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ============================================================================
// PREVIEW SECTION
// ============================================================================

class OnboardingStateProvider : PreviewParameterProvider<OnboardingState> {
    override val values = sequenceOf(
        OnboardingState(),
        OnboardingState(
            gender = Gender.MALE,
            weightKg = 80,
            wakeUpHour = 6,
            wakeUpMinute = 30,
            sleepHour = 22,
            sleepMinute = 30
        ),
        OnboardingState(
            gender = Gender.FEMALE,
            weightKg = 55,
            wakeUpHour = 7,
            wakeUpMinute = 0,
            sleepHour = 23,
            sleepMinute = 0
        ),
        OnboardingState(
            gender = Gender.MALE,
            weightKg = 70,
            isLoading = true
        ),
        OnboardingState(
            gender = Gender.FEMALE,
            weightKg = 60,
            errorMessage = "Không thể lưu dữ liệu. Vui lòng thử lại."
        )
    )
}

@Preview(name = "Light Mode - Default", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewOnboardingScreenLight() {
    BlinkDrinkTheme {
        OnboardingScreenContent(
            state = OnboardingState(),
            onGenderChange = {},
            onWeightChange = {},
            onWakeUpTimeClick = {},
            onSleepTimeClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(name = "Dark Mode - Default", showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewOnboardingScreenDark() {
    BlinkDrinkTheme(darkTheme = true) {
        OnboardingScreenContent(
            state = OnboardingState(),
            onGenderChange = {},
            onWeightChange = {},
            onWakeUpTimeClick = {},
            onSleepTimeClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(name = "Male Selected", showBackground = true)
@Composable
private fun PreviewOnboardingMale() {
    BlinkDrinkTheme {
        OnboardingScreenContent(
            state = OnboardingState(gender = Gender.MALE, weightKg = 75),
            onGenderChange = {},
            onWeightChange = {},
            onWakeUpTimeClick = {},
            onSleepTimeClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(name = "Female Selected", showBackground = true)
@Composable
private fun PreviewOnboardingFemale() {
    BlinkDrinkTheme {
        OnboardingScreenContent(
            state = OnboardingState(gender = Gender.FEMALE, weightKg = 60),
            onGenderChange = {},
            onWeightChange = {},
            onWakeUpTimeClick = {},
            onSleepTimeClick = {},
            onSaveClick = {}
        )
    }
}