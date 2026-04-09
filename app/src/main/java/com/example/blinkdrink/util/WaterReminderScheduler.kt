// ============================================================================
// FILE: util/WaterReminderScheduler.kt
// ============================================================================
package com.example.blinkdrink.util

import android.content.Context
import androidx.work.*
import com.example.blinkdrink.domain.model.ReminderTime
import com.example.blinkdrink.worker.WaterReminderWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

object WaterReminderScheduler {

    private const val WATER_REMINDER_TAG = "water_reminder"

    /**
     * ⏰ Lên lịch tất cả các nhắc nhở uống nước trong ngày
     */
    fun scheduleAllWaterReminders(context: Context, reminders: List<ReminderTime>) {
        // Hủy tất cả lịch cũ
        cancelAllWaterReminders(context)

        // Chỉ lên lịch cho các reminder được bật
        reminders.filter { it.isEnabled }.forEach { reminder ->
            scheduleWaterReminder(context, reminder)
        }
    }

    /**
     * ⏰ Lên lịch 1 nhắc nhở uống nước
     */
    private fun scheduleWaterReminder(context: Context, reminder: ReminderTime) {
        val currentTime = Calendar.getInstance()
        val reminderTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, reminder.hour)
            set(Calendar.MINUTE, reminder.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Nếu giờ nhắc đã qua, lên lịch cho ngày mai
        if (reminderTime.before(currentTime)) {
            reminderTime.add(Calendar.DAY_OF_MONTH, 1)
        }

        val delayInMillis = reminderTime.timeInMillis - currentTime.timeInMillis

        val workRequest = OneTimeWorkRequestBuilder<WaterReminderWorker>()
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "reminder_time" to String.format("%02d:%02d", reminder.hour, reminder.minute)
                )
            )
            .addTag(WATER_REMINDER_TAG)
            .addTag("water_reminder_${reminder.id}")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "water_reminder_${reminder.id}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    /**
     * 🔕 Hủy tất cả lịch nhắc uống nước
     */
    fun cancelAllWaterReminders(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(WATER_REMINDER_TAG)
    }
}