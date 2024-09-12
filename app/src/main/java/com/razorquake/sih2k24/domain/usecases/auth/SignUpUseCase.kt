package com.razorquake.sih2k24.domain.usecases.auth

import com.google.firebase.auth.FirebaseAuth
import com.razorquake.sih2k24.domain.repository.AuthRepository
import com.razorquake.sih2k24.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) {
    suspend operator fun invoke(username: String, email: String, password: String): Result<Unit> = runCatching {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val userId = result.user?.uid ?: throw Exception("User ID is null")
        userRepository.createUser(userId, username, email)
        authRepository.setUserAuthenticated(true)

    }

}