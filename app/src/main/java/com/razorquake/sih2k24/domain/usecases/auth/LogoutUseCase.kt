package com.razorquake.sih2k24.domain.usecases.auth

import com.google.firebase.auth.FirebaseAuth
import com.razorquake.sih2k24.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        firebaseAuth.signOut()
        authRepository.setUserAuthenticated(false)
    }
}