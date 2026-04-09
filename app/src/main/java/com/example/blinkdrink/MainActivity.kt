// ============================================================================
// FILE: MainActivity.kt
// ============================================================================
package com.example.blinkdrink

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.blinkdrink.domain.usecase.user.CheckOnboardingStatusUseCase
import com.example.blinkdrink.navigation.AppNavHost
import com.example.blinkdrink.ui.theme.BlinkDrinkTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var checkOnboardingStatusUseCase: CheckOnboardingStatusUseCase

    // ✅ 1. XIN QUYỀN VỊ TRÍ
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Đã được cấp quyền chính xác
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Coarse location access granted
            }
            else -> {
                // No location access granted
            }
        }
    }

    // ✅ 2. XIN QUYỀN NOTIFICATION (Android 13+)
    private val notificationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Notification permission granted
        } else {
            // Notification permission denied
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Yêu cầu các quyền cần thiết
        checkAndRequestPermissions()

        var isOnboardingCompleted by mutableStateOf<Boolean?>(null)

        lifecycleScope.launch {
            isOnboardingCompleted = checkOnboardingStatusUseCase()
        }

        // ✅ 3. LẤY ACTION TỪ NOTIFICATION (nếu có)
        val notificationAction = intent.getStringExtra("action")

        setContent {
            BlinkDrinkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    isOnboardingCompleted?.let { completed ->
                        AppNavHost(
                            startWithOnboarding = !completed,
                            notificationAction = notificationAction // ✅ Truyền xuống NavHost
                        )
                    }
                }
            }
        }
    }

    // ✅ 4. XỬ LÝ KHI CÓ INTENT MỚI (App đang chạy)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        // ✅ Xử lý action từ notification khi app đang mở
        val action = intent.getStringExtra("action")
        if (action != null) {
            // TODO: Gửi event đến ViewModel hoặc Navigation
            // Có thể dùng EventBus, SharedFlow, hoặc SingleLiveEvent
        }
    }

    // ✅ 5. YÊU CẦU QUYỀN VỊ TRÍ
    private fun checkAndRequestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
            }
            else -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    // ✅ 6. YÊU CẦU QUYỀN NOTIFICATION (Android 13+)
    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Giải thích tại sao cần quyền này
                    // Có thể hiển thị dialog
                    notificationPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    // Yêu cầu quyền lần đầu
                    notificationPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    // ✅ 7. YÊU CẦU TẤT CẢ QUYỀN
    private fun checkAndRequestPermissions() {
        checkAndRequestLocationPermission()
        checkAndRequestNotificationPermission()
    }
}