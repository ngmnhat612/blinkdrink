// ============================================================================
// FILE: domain/usecase/user/GetUserProfileUseCase.kt
// ============================================================================
package com.example.blinkdrink.domain.usecase.user

import com.example.blinkdrink.domain.model.UserProfile
import com.example.blinkdrink.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserProfile?> {
        return userRepository.getUserProfile()
    }
}