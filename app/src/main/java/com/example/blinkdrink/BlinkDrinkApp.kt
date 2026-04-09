// ============================================================================
// FILE: BlinkDrinkApp.kt
// ============================================================================
package com.example.blinkdrink

import android.app.Application
import com.example.blinkdrink.util.EyeBreakNotificationHelper
import com.example.blinkdrink.util.WaterReminderNotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BlinkDrinkApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // ✅ Tạo notification channels khi app khởi động
        EyeBreakNotificationHelper.createNotificationChannel(this)
        WaterReminderNotificationHelper.createNotificationChannel(this)
    }
}