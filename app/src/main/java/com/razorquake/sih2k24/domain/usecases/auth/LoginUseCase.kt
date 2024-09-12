package com.razorquake.sih2k24.domain.usecases.auth

import com.google.firebase.auth.FirebaseAuth
import com.razorquake.sih2k24.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit>  = runCatching {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        authRepository.setUserAuthenticated(true)
    }
}