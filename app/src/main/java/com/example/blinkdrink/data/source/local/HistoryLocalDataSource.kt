// ============================================================================
// FILE 4: data/source/local/HistoryLocalDataSource.kt
// ============================================================================
package com.example.blinkdrink.data.source.local

import com.example.blinkdrink.data.source.local.datastore.DataStoreManager
import com.example.blinkdrink.data.source.local.datastore.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryLocalDataSource @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    fun getHistoryRecordsJson(): Flow<String> {
        return dataStoreManager.getValue(PreferencesKeys.HISTORY_RECORDS_JSON, "")
    }

    suspend fun setHistoryRecordsJson(json: String) {
        dataStoreManager.setValue(PreferencesKeys.HISTORY_RECORDS_JSON, json)
    }
}