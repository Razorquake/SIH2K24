package com.razorquake.sih2k24.domain.usecases.auth

import com.razorquake.sih2k24.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Flow<Boolean> = authRepository.isUserAuthenticated()
}