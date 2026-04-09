// ============================================================================
// FILE: domain/usecase/user/CheckOnboardingStatusUseCase.kt
// ============================================================================
package com.example.blinkdrink.domain.usecase.user

import com.example.blinkdrink.domain.repository.UserRepository
import javax.inject.Inject

class CheckOnboardingStatusUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Boolean {
        return userRepository.isOnboardingCompleted()
    }
}