package com.razorquake.sih2k24.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun setUserAuthenticated(isAuthenticated: Boolean)
    suspend fun isUserAuthenticated(): Flow<Boolean>
}