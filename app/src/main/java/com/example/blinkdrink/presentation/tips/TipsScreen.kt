// ============================================================================
// FILE: presentation/tips/TipsScreen.kt
// ============================================================================
package com.example.blinkdrink.presentation.tips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blinkdrink.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.neutralBackground)
    ) {
        // Top Bar
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Lời khuyên & Bài tập",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.neutralSurface
            )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // --- SECTION 1: NƯỚC ---
            SectionHeader(
                title = "Lời khuyên khi uống nước",
                icon = Icons.Filled.WaterDrop,
                color = MaterialTheme.colorScheme.waterPrimary
            )

            waterTips.forEach { tip ->
                TipCard(
                    content = tip,
                    icon = Icons.Outlined.WaterDrop,
                    accentColor = MaterialTheme.colorScheme.waterPrimary,
                    backgroundColor = MaterialTheme.colorScheme.waterBackground
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- SECTION 2: MẮT ---
            SectionHeader(
                title = "Bài tập cho mắt",
                icon = Icons.Filled.Visibility,
                color = MaterialTheme.colorScheme.eyeBreakPrimary
            )

            eyeExercises.forEach { exercise ->
                TipCard(
                    content = exercise,
                    icon = Icons.Outlined.Visibility,
                    accentColor = MaterialTheme.colorScheme.eyeBreakPrimary,
                    backgroundColor = MaterialTheme.colorScheme.eyeBreakBackground
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = MaterialTheme.colorScheme.textTertiary
        )
    }
}

@Composable
private fun TipCard(
    content: String,
    icon: ImageVector,
    accentColor: Color,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.neutralSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.textSecondary
                )
            )
        }
    }
}

// ============================================================================
// PREVIEW TipsScreen
// ============================================================================
@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 1200,
    name = "Tips Screen - Light Mode"
)
@Composable
private fun PreviewTipsScreenLight() {
    com.example.blinkdrink.ui.theme.BlinkDrinkTheme(darkTheme = false) {
        TipsScreen()
    }
}

@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 1200,
    name = "Tips Screen - Dark Mode"
)
@Composable
private fun PreviewTipsScreenDark() {
    com.example.blinkdrink.ui.theme.BlinkDrinkTheme(darkTheme = true) {
        TipsScreen()
    }
}