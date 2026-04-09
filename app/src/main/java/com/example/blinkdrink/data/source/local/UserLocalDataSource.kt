// ============================================================================
// FILE: data/source/local/UserLocalDataSource.kt
// ============================================================================
package com.example.blinkdrink.data.source.local

import com.example.blinkdrink.data.source.local.datastore.DataStoreManager
import com.example.blinkdrink.data.source.local.datastore.PreferencesKeys
import com.example.blinkdrink.domain.model.Gender
import com.example.blinkdrink.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    fun getUserProfile(): Flow<UserProfile?> {
        return combine(
            dataStoreManager.getValue(PreferencesKeys.USER_GENDER, ""),
            dataStoreManager.getValue(PreferencesKeys.USER_WEIGHT, 0),
            dataStoreManager.getValue(PreferencesKeys.WAKE_UP_HOUR, 6),
            dataStoreManager.getValue(PreferencesKeys.WAKE_UP_MINUTE, 0),
            dataStoreManager.getValue(PreferencesKeys.SLEEP_HOUR, 22),
            dataStoreManager.getValue(PreferencesKeys.SLEEP_MINUTE, 0),
            dataStoreManager.getValue(PreferencesKeys.DAILY_WATER_GOAL, 2530),
            dataStoreManager.getValue(PreferencesKeys.IS_ONBOARDING_COMPLETED, false)
        ) { flows: Array<Any?> ->
            val gender = flows[0] as String
            val weight = flows[1] as Int
            val wakeHour = flows[2] as Int
            val wakeMin = flows[3] as Int
            val sleepHour = flows[4] as Int
            val sleepMin = flows[5] as Int
            val waterGoal = flows[6] as Int
            val onboardingCompleted = flows[7] as Boolean

            if (gender.isEmpty() || weight == 0) {
                null
            } else {
                UserProfile(
                    gender = if (gender == "Nam") Gender.MALE else Gender.FEMALE,
                    weightKg = weight,
                    wakeUpHour = wakeHour,
                    wakeUpMinute = wakeMin,
                    sleepHour = sleepHour,
                    sleepMinute = sleepMin,
                    dailyWaterGoalMl = waterGoal,
                    isOnboardingCompleted = onboardingCompleted
                )
            }
        }
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        dataStoreManager.setValue(PreferencesKeys.USER_GENDER, if (profile.gender == Gender.MALE) "Nam" else "Nữ")
        dataStoreManager.setValue(PreferencesKeys.USER_WEIGHT, profile.weightKg)
        dataStoreManager.setValue(PreferencesKeys.WAKE_UP_HOUR, profile.wakeUpHour)
        dataStoreManager.setValue(PreferencesKeys.WAKE_UP_MINUTE, profile.wakeUpMinute)
        dataStoreManager.setValue(PreferencesKeys.SLEEP_HOUR, profile.sleepHour)
        dataStoreManager.setValue(PreferencesKeys.SLEEP_MINUTE, profile.sleepMinute)
        dataStoreManager.setValue(PreferencesKeys.DAILY_WATER_GOAL, profile.dailyWaterGoalMl)
    }

    suspend fun isOnboardingCompleted(): Boolean {
        return dataStoreManager.getValue(PreferencesKeys.IS_ONBOARDING_COMPLETED, false).first()
    }

    suspend fun completeOnboarding() {
        dataStoreManager.setValue(PreferencesKeys.IS_ONBOARDING_COMPLETED, true)
    }
}