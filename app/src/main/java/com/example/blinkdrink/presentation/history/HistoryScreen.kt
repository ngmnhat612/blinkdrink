// ============================================================================
// FILE: presentation/history/HistoryScreen.kt
// ============================================================================
package com.example.blinkdrink.presentation.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.blinkdrink.presentation.components.BottomNavigationBar
import com.example.blinkdrink.presentation.history.components.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    HistoryScreenContent(
        state = state,
        navController = navController,
        onTabChange = { viewModel.changeTab(it) },
        onPrevClick = { viewModel.navigateToPrevious() },
        onNextClick = { viewModel.navigateToNext() }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenContent(
    state: HistoryState,
    navController: NavController,
    onTabChange: (Int) -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Thống kê",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF8F9FC)
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) },
        containerColor = Color(0xFFF8F9FC)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            DateHeaderNavigator(
                currentDate = state.currentDate,
                isMonthView = state.selectedTab == 0,
                onPrevClick = onPrevClick,
                onNextClick = onNextClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                SegmentedControl(
                    selectedTab = state.selectedTab,
                    onTabChange = onTabChange
                )
                Spacer(modifier = Modifier.height(32.dp))

                // ✅ CHỌN VERSION CHART PHÙ HỢP
                // Option 1: BarChart (Compose layout)
                BarChart(
                    data = state.chartData,
                    isMonthView = state.selectedTab == 0,
                    currentMonth = state.currentDate.monthValue,
                    currentYear = state.currentDate.year // ✅ THÊM PARAM
                )

                // Option 2: BarChartPerfect (Canvas thuần)
                // Uncomment nếu muốn dùng version Canvas 100%
                /*
                BarChartPerfect(
                    data = state.chartData,
                    isMonthView = state.selectedTab == 0,
                    currentMonth = state.currentDate.monthValue,
                    currentYear = state.currentDate.year
                )
                */
            }

            Spacer(modifier = Modifier.height(32.dp))
            PaddingBox(horizontal = 20.dp) {
                SectionTitle("Tiến độ tuần này")
            }
            Spacer(modifier = Modifier.height(16.dp))
            PaddingBox(horizontal = 8.dp) {
                WeeklyProgressSection(weekProgress = state.weeklyProgress)
            }

            Spacer(modifier = Modifier.height(32.dp))
            PaddingBox(horizontal = 20.dp) {
                SectionTitle("Tổng quan")
                Spacer(modifier = Modifier.height(16.dp))
                WaterReportSection(statistics = state.statistics)
            }

            Spacer(modifier = Modifier.height(32.dp))
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun PaddingBox(horizontal: androidx.compose.ui.unit.Dp, content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = horizontal)) {
        content()
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF2D3436)
    )
}