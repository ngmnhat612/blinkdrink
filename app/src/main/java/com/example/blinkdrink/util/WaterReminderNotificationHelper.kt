// ============================================================================
// FILE: util/WaterReminderNotificationHelper.kt
// ============================================================================
package com.example.blinkdrink.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.blinkdrink.MainActivity
import com.example.blinkdrink.R

object WaterReminderNotificationHelper {

    private const val CHANNEL_ID = "water_reminder_channel"
    private const val CHANNEL_NAME = "Nhắc uống nước"
    const val NOTIFICATION_ID_WATER_REMINDER = 2001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tạo Notification Channel (Yêu cầu bắt buộc từ Android 8.0+)
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Thông báo nhắc nhở uống nước"
                enableVibration(true)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * 🔔 Notification nhắc uống nước
     */
    fun showWaterReminderNotification(context: Context, reminderTime: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("action", "drink_water")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Hiển thị thông báo
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("💧 Đã đến giờ uống nước!")
            .setContentText("Nhấn để ghi nhận lượng nước đã uống")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID_WATER_REMINDER, notification)
    }

    fun cancelNotification(context: Context) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.cancel(NOTIFICATION_ID_WATER_REMINDER)
    }
}