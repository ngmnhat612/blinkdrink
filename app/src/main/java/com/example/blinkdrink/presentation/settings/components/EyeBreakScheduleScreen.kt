// ============================================================================
// FILE: presentation/settings/components/EyeBreakScheduleScreen.kt
// ============================================================================
package com.example.blinkdrink.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.blinkdrink.domain.model.FrequencyOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EyeBreakScheduleScreen(
    navController: NavController,
    viewModel: EyeBreakScheduleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val frequencyOptions = listOf(
        FrequencyOption(1, "1 lần/giờ", "(Mỗi 60 phút)"),
        FrequencyOption(2, "2 lần/giờ", "(Mỗi 30 phút)"),
        FrequencyOption(3, "3 lần/giờ", "(Mỗi 20 phút)"),
        FrequencyOption(4, "4 lần/giờ", "(Mỗi 15 phút)"),
        FrequencyOption(5, "5 lần/giờ", "(Mỗi 12 phút)"),
        FrequencyOption(6, "6 lần/giờ", "(Mỗi 10 phút)")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tần suất nghỉ mắt", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Đóng", tint = Color.Gray)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Text(
                text = "Chọn tần suất nhắc nhở để bảo vệ mắt của bạn khi làm việc liên tục.",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp)
            )

            Divider(color = Color.LightGray.copy(alpha = 0.5f))

            LazyColumn {
                items(frequencyOptions) { option ->
                    FrequencyItemRow(
                        option = option,
                        isSelected = (option.frequencyPerHour == state.selectedFrequency),
                        onSelect = { viewModel.updateFrequency(option.frequencyPerHour) }
                    )
                    Divider(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FrequencyItemRow(
    option: FrequencyOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color(0xFFE3F2FD) else Color.White)
            .clickable { onSelect() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = option.label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            Text(text = option.subLabel, fontSize = 13.sp, color = Color.Gray)
        }

        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF42A5F5),
                unselectedColor = Color.LightGray
            )
        )
    }
}