package com.razorquake.sih2k24.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.razorquake.sih2k24.domain.models.User
import com.razorquake.sih2k24.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun createUser(userId: String, username: String, email: String) {
        val user = User(userId, username, email)
        firestore.collection("users").document(userId).set(user).await()
    }

    override suspend fun getUserInfo(userId: String): User? {
        return firestore.collection("users").document(userId).get().await().toObject(User::class.java)
    }

    override suspend fun updateUserInfo(userId: String, updates: Map<String, Any>) {
        firestore.collection("users").document(userId).update(updates).await()
    }
}