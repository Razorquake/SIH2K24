package com.razorquake.sih2k24.domain.usecases.user

import com.razorquake.sih2k24.domain.models.User
import com.razorquake.sih2k24.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, updates: Map<String, Any>): Result<Unit> = runCatching {
        userRepository.updateUserInfo(userId, updates)
    }
}