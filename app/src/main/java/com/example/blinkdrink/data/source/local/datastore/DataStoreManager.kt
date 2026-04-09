// ============================================================================
// FILE: data/source/local/datastore/DataStoreManager.kt
// ============================================================================
package com.example.blinkdrink.data.source.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "blinkdrink_settings")

@Singleton
class DataStoreManager @Inject constructor(
    private val context: Context
) {
    private val dataStore = context.dataStore

    // Lưu trữ key-value với DataStore
    suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun <T> getValue(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}