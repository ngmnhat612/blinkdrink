// ============================================================================
// FILE: util/EyeBreakScheduler.kt
// ============================================================================
package com.example.blinkdrink.util

import android.content.Context
import androidx.work.*
import com.example.blinkdrink.worker.EyeBreakWorker
import java.util.concurrent.TimeUnit

object EyeBreakScheduler {

    private const val REMIND_WORK_TAG = "eye_break_remind"
    private const val FINISH_WORK_TAG = "eye_break_finish"
    private const val MIN_TIME_BEFORE_SESSION_END = 15

    /**
     * ⏰ Lên lịch tất cả các notification và TRẢ VỀ số lần thực tế
     * @return Int - Số lần nghỉ thực tế được lên lịch
     */
    fun scheduleAllEyeBreakReminders(
        context: Context,
        sessionDurationMinutes: Int,
        breakDurationMinutes: Int,
        frequency: Int
    ): Int {
        cancelAllSchedules(context)

        val breakIntervalMinutes = 60 / frequency
        val maxPossibleBreaks = calculateMaxBreaks(
            sessionDurationMinutes,
            breakIntervalMinutes,
            breakDurationMinutes
        )

        var actualScheduledBreaks = 0

        for (i in 1..maxPossibleBreaks) {
            val delayMinutes = breakIntervalMinutes * i
            val timeAfterBreak = delayMinutes + breakDurationMinutes
            val remainingTime = sessionDurationMinutes - timeAfterBreak

            if (remainingTime >= MIN_TIME_BEFORE_SESSION_END) {
                scheduleBreakReminder(context, delayMinutes, i)
                actualScheduledBreaks++ // ✅ Đếm số lần thực tế
            }
        }

        // ✅ RETURN số lần thực tế đã lên lịch
        return actualScheduledBreaks
    }

    /**
     * ✅ Tính số lần nghỉ THỰC TẾ có thể lên lịch
     * (Áp dụng logic tránh thông báo gần cuối phiên)
     */
    fun calculateActualExpectedBreaks(
        sessionDurationMinutes: Int,
        breakDurationMinutes: Int,
        frequency: Int
    ): Int {
        if (sessionDurationMinutes == 0 || frequency == 0) return 0

        val breakIntervalMinutes = 60 / frequency
        return calculateMaxBreaks(
            sessionDurationMinutes,
            breakIntervalMinutes,
            breakDurationMinutes
        )
    }

    private fun calculateMaxBreaks(
        sessionDurationMinutes: Int,
        breakIntervalMinutes: Int,
        breakDurationMinutes: Int
    ): Int {
        var count = 0
        var currentTime = 0

        while (true) {
            currentTime += breakIntervalMinutes

            if (currentTime >= sessionDurationMinutes) break

            val timeAfterBreak = currentTime + breakDurationMinutes
            val remainingTime = sessionDurationMinutes - timeAfterBreak

            if (remainingTime >= MIN_TIME_BEFORE_SESSION_END) {
                count++
            } else {
                break
            }
        }

        return count
    }

    fun scheduleBreakReminder(context: Context, delayMinutes: Int, breakNumber: Int = 0) {
        val uniqueWorkName = if (breakNumber > 0) {
            "break_reminder_$breakNumber"
        } else {
            "break_reminder_next"
        }

        val workRequest = OneTimeWorkRequestBuilder<EyeBreakWorker>()
            .setInitialDelay(delayMinutes.toLong(), TimeUnit.MINUTES)
            .setInputData(workDataOf("action" to "remind_break"))
            .addTag(REMIND_WORK_TAG)
            .apply {
                if (breakNumber > 0) {
                    addTag("break_reminder_$breakNumber")
                }
            }
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            uniqueWorkName,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun scheduleBreakFinished(context: Context, delayMinutes: Int) {
        val workRequest = OneTimeWorkRequestBuilder<EyeBreakWorker>()
            .setInitialDelay(delayMinutes.toLong(), TimeUnit.MINUTES)
            .setInputData(workDataOf("action" to "finish_break"))
            .addTag(FINISH_WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            FINISH_WORK_TAG,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelAllSchedules(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(REMIND_WORK_TAG)
        WorkManager.getInstance(context).cancelAllWorkByTag(FINISH_WORK_TAG)
    }
}