package com.razorquake.sih2k24.domain.repository

import com.razorquake.sih2k24.domain.models.User

interface UserRepository {
    suspend fun createUser(userId: String, username: String, email: String)
    suspend fun getUserInfo(userId: String): User?
    suspend fun updateUserInfo(userId: String, updates: Map<String, Any>)

}