// ============================================================================
// FILE: worker/EyeBreakWorker.kt
// ============================================================================
package com.example.blinkdrink.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.blinkdrink.data.source.local.datastore.DataStoreManager
import com.example.blinkdrink.data.source.local.datastore.PreferencesKeys
import com.example.blinkdrink.util.EyeBreakNotificationHelper
import kotlinx.coroutines.flow.first

class EyeBreakWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val action = inputData.getString("action") ?: return Result.failure()
        val dataStoreManager = DataStoreManager(applicationContext)

        when (action) {
            "remind_break" -> {
                EyeBreakNotificationHelper.showBreakReminderNotification(applicationContext)
            }

            "finish_break" -> {
                val breakCount = dataStoreManager.getValue(
                    PreferencesKeys.DAILY_EYE_BREAK_COUNT,
                    0
                ).first()

                // ✅ LẤY expectedBreaks THỰC TẾ từ DataStore
                val expectedBreaks = dataStoreManager.getValue(
                    PreferencesKeys.EXPECTED_BREAK_COUNT, // ✅ Key mới
                    0
                ).first()

                EyeBreakNotificationHelper.showBreakFinishedNotification(
                    applicationContext,
                    breakCount,
                    expectedBreaks
                )
            }
        }

        return Result.success()
    }
}