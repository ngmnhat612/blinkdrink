// ============================================================================
// FILE: domain/repository/UserRepository.kt
// ============================================================================
package com.example.blinkdrink.domain.repository

import com.example.blinkdrink.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(): Flow<UserProfile?>
    suspend fun saveUserProfile(profile: UserProfile)
    suspend fun isOnboardingCompleted(): Boolean
    suspend fun completeOnboarding()
}