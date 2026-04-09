// ============================================================================
// FILE: worker/WaterReminderWorker.kt
// ============================================================================
package com.example.blinkdrink.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.blinkdrink.util.WaterReminderNotificationHelper

// Worker thực hiện gửi thông báo
class WaterReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val reminderTime = inputData.getString("reminder_time") ?: return Result.failure()

        // Gửi notification nhắc uống nước
        WaterReminderNotificationHelper.showWaterReminderNotification(
            applicationContext,
            reminderTime
        )

        return Result.success()
    }
}