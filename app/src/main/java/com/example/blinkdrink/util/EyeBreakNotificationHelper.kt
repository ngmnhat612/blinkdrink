// ============================================================================
// FILE: util/EyeBreakNotificationHelper.kt
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

object EyeBreakNotificationHelper {

    private const val CHANNEL_ID = "eye_break_channel"
    private const val CHANNEL_NAME = "Nghỉ mắt"

    const val NOTIFICATION_ID_BREAK_REMINDER = 1001
    const val NOTIFICATION_ID_BREAK_FINISHED = 1002

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Thông báo nhắc nghỉ mắt"
                enableVibration(true)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * 🔔 Notification nhắc đến giờ nghỉ mắt
     */
    fun showBreakReminderNotification(context: Context) {
        // Tạo Intent để mở MainActivity
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("action", "confirm_eye_break") // ✅ Để MainActivity xử lý
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Cần tạo icon
            .setContentTitle("⏰ Đến giờ nghỉ mắt!")
            .setContentText("Đã làm việc đủ thời gian. Nhấn để xác nhận nghỉ mắt.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID_BREAK_REMINDER, notification)
    }

    /**
     * 🎉 Notification khi hết giờ nghỉ
     */
    fun showBreakFinishedNotification(context: Context, breakCount: Int, expectedBreaks: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("✅ Hoàn thành nghỉ mắt!")
            .setContentText("Tiếp tục làm việc nhé!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID_BREAK_FINISHED, notification)
    }

    /**
     * 🔕 Hủy notification
     */
    fun cancelNotification(context: Context, notificationId: Int) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.cancel(notificationId)
    }
}