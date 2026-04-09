// ============================================================================
// FILE: presentation/history/components/DateHeaderNavigator.kt
// ============================================================================
package com.example.blinkdrink.presentation.history.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateHeaderNavigator(
    currentDate: LocalDate,
    isMonthView: Boolean,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val displayText = if (isMonthView) {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("vi", "VN"))
        currentDate.format(formatter).replaceFirstChar { it.uppercase() }
    } else {
        val formatter = DateTimeFormatter.ofPattern("yyyy")
        "Năm ${currentDate.format(formatter)}"
    }
    val buttonShape = RoundedCornerShape(12.dp)
    val borderColor = Color(0xFFEEEEEE)
    val iconColor = Color(0xFF757575)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Nút TRÁI ---
        IconButton(
            onClick = onPrevClick,
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, borderColor, buttonShape)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Previous",
                tint = iconColor
            )
        }
        Text(
            text = displayText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        IconButton(
            onClick = onNextClick,
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, borderColor, buttonShape)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next",
                tint = iconColor
            )
        }
    }
}