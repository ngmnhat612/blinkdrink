// ============================================================================
// FILE: data/repository/UserRepositoryImpl.kt
// ============================================================================
package com.example.blinkdrink.data.repository

import com.example.blinkdrink.data.source.local.UserLocalDataSource
import com.example.blinkdrink.domain.model.UserProfile
import com.example.blinkdrink.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource
) : UserRepository {

    override fun getUserProfile(): Flow<UserProfile?> {
        return localDataSource.getUserProfile()
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        localDataSource.saveUserProfile(profile)
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        return localDataSource.isOnboardingCompleted()
    }

    override suspend fun completeOnboarding() {
        localDataSource.completeOnboarding()
    }
}