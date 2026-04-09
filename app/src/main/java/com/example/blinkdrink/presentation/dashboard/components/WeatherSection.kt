// ============================================================================
// FILE: presentation/dashboard/components/WeatherSection.kt
// ============================================================================
package com.example.blinkdrink.presentation.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blinkdrink.domain.model.WeatherInfo
import com.example.blinkdrink.ui.theme.waterPrimary

@Composable
fun WeatherSection(
    weatherInfo: WeatherInfo = WeatherInfo(),
    isLoading: Boolean = false,
    hasAdjustedToday: Boolean = false,
    onWaterAdjustmentClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // ✅ THAY ĐỔI: Dùng theme color
    val primaryColor = MaterialTheme.colorScheme.waterPrimary

    // ✅ MÀU NỀN ĐỒNG NHẤT
    val backgroundColor = Color.White

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = 2.dp
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "NHIỆT ĐỘ TRUNG BÌNH TRONG NGÀY",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF546E7A),
                                lineHeight = 11.sp
                            )
                        }

                        Text(
                            text = weatherInfo.location.substringBefore(","),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF455A64),
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(includeFontPadding = false)
                            ),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "${weatherInfo.temperature}°C",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(includeFontPadding = false),
                                lineHeight = 35.sp
                            )
                        )

                        Text(
                            text = weatherInfo.condition,
                            fontSize = 14.sp,
                            color = Color(0xFF78909C),
                            fontWeight = FontWeight.Medium,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(includeFontPadding = false)
                            ),
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }

                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 1.dp,
                    color = Color.Gray.copy(alpha = 0.3f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = primaryColor.copy(alpha = 0.2f),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(weatherInfo.weatherIcon, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = weatherInfo.tipsMessage,
                                fontSize = 11.sp,
                                color = primaryColor.copy(alpha = 0.8f),
                                lineHeight = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (hasAdjustedToday) {
                            Color(0xFFE0E0E0)
                        } else {
                            Color.White
                        },
                        shadowElevation = if (hasAdjustedToday) 0.dp else 1.dp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickable(enabled = !hasAdjustedToday) {
                                onWaterAdjustmentClick(weatherInfo.waterAdjustment)
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${weatherInfo.waterAdjustment}ml",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (hasAdjustedToday) {
                                    Color(0xFF9E9E9E)
                                } else if (weatherInfo.waterAdjustment >= 0) {
                                    primaryColor
                                } else {
                                    primaryColor.copy(alpha = 0.8f)
                                },
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                                )
                            )
                            Text(
                                text = if (hasAdjustedToday) "Đã bổ sung" else "Bổ sung",
                                fontSize = 8.sp,
                                color = if (hasAdjustedToday) Color(0xFF9E9E9E) else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}