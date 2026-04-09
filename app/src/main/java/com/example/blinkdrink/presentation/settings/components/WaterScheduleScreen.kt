// ============================================================================
// FILE: presentation/settings/components/WaterScheduleScreen.kt
// ============================================================================
package com.example.blinkdrink.presentation.settings.components

import android.app.TimePickerDialog
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.blinkdrink.domain.model.ReminderTime
import com.example.blinkdrink.ui.theme.waterPrimary
import com.example.blinkdrink.ui.theme.waterOnPrimary
import com.example.blinkdrink.ui.theme.textPrimary
import com.example.blinkdrink.ui.theme.textSecondary
import com.example.blinkdrink.ui.theme.neutralSurface
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterScheduleScreen(
    navController: NavController,
    viewModel: WaterScheduleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // State cho dialog xác nhận xóa
    var showDeleteDialog by remember { mutableStateOf(false) }
    var reminderToDelete by remember { mutableStateOf<ReminderTime?>(null) }

    fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                viewModel.addReminder(selectedHour, selectedMinute)
            },
            currentHour,
            currentMinute,
            true
        ).show()
    }

    // Dialog xác nhận xóa
    if (showDeleteDialog && reminderToDelete != null) {
        DeleteConfirmationDialog(
            reminderTime = String.format("%02d:%02d", reminderToDelete!!.hour, reminderToDelete!!.minute),
            onDismiss = {
                showDeleteDialog = false
                reminderToDelete = null
            },
            onConfirm = {
                viewModel.deleteReminder(reminderToDelete!!.id)
                showDeleteDialog = false
                reminderToDelete = null
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Lịch nhắc nhở",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.textPrimary
                    )
                },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = MaterialTheme.colorScheme.textSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.neutralSurface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showTimePicker() },
                containerColor = MaterialTheme.colorScheme.waterPrimary,
                contentColor = MaterialTheme.colorScheme.waterOnPrimary,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Thêm nhắc nhở",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.neutralSurface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "Tùy chỉnh lịch uống nước phù hợp với thời gian biểu của bạn.",
                color = MaterialTheme.colorScheme.textSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Divider(
                color = MaterialTheme.colorScheme.textSecondary.copy(alpha = 0.1f)
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.waterPrimary
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = state.reminders,
                        key = { it.id }
                    ) { item ->
                        SwipeToDeleteReminderItem(
                            item = item,
                            onToggle = { isChecked ->
                                viewModel.toggleReminder(item.id, isChecked)
                            },
                            onDelete = {
                                reminderToDelete = item
                                showDeleteDialog = true
                            }
                        )
                        Divider(
                            color = MaterialTheme.colorScheme.textSecondary.copy(alpha = 0.1f),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDeleteReminderItem(
    item: ReminderTime,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            when (dismissValue) {
                DismissValue.DismissedToEnd -> {
                    onDelete()
                    false // Không tự động xóa, để dialog xác nhận
                }
                DismissValue.DismissedToStart -> {
                    onDelete()
                    false // Không tự động xóa, để dialog xác nhận
                }
                else -> false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(
            DismissDirection.StartToEnd,
            DismissDirection.EndToStart
        ),
        background = {
            val color by animateColorAsState(
                targetValue = when (dismissState.dismissDirection) {
                    DismissDirection.StartToEnd -> MaterialTheme.colorScheme.error
                    DismissDirection.EndToStart -> MaterialTheme.colorScheme.error
                    else -> Color.Transparent
                },
                label = "background color"
            )

            val scale by animateFloatAsState(
                targetValue = if (dismissState.targetValue != DismissValue.Default) 1.3f else 1f,
                label = "icon scale"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 24.dp),
                contentAlignment = when (dismissState.dismissDirection) {
                    DismissDirection.StartToEnd -> Alignment.CenterStart
                    DismissDirection.EndToStart -> Alignment.CenterEnd
                    else -> Alignment.Center
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Xóa",
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = {
            ReminderItemRow(
                item = item,
                onToggle = onToggle
            )
        }
    )
}

@Composable
fun ReminderItemRow(
    item: ReminderTime,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.neutralSurface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = String.format("%02d:%02d", item.hour, item.minute),
            fontSize = 18.sp,
            color = if (item.isEnabled) {
                MaterialTheme.colorScheme.textPrimary
            } else {
                MaterialTheme.colorScheme.textSecondary.copy(alpha = 0.5f)
            }
        )

        Switch(
            checked = item.isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.waterOnPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.waterPrimary,
                uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                uncheckedTrackColor = MaterialTheme.colorScheme.textSecondary.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
fun DeleteConfirmationDialog(
    reminderTime: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Xóa nhắc nhở?",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.textPrimary
            )
        },
        text = {
            Text(
                text = "Bạn có chắc chắn muốn xóa nhắc nhở lúc $reminderTime?",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.textSecondary
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("XÓA", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "HỦY",
                    color = MaterialTheme.colorScheme.textSecondary
                )
            }
        }
    )
}