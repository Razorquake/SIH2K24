package com.razorquake.sih2k24.domain.usecases.user

import com.razorquake.sih2k24.domain.models.User
import com.razorquake.sih2k24.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<User> = runCatching {
        userRepository.getUserInfo(userId) ?: throw Exception("User not found")
    }
}