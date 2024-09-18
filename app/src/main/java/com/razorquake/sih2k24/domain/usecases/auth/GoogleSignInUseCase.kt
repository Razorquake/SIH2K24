package com.razorquake.sih2k24.domain.usecases.auth

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.razorquake.sih2k24.Constants
import com.razorquake.sih2k24.domain.repository.AuthRepository
import com.razorquake.sih2k24.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth,
) {
    suspend fun getGoogleSignInCredential(activity: Activity): GetCredentialResponse {
        val credentialManager = CredentialManager.create(activity)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(Constants.WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return credentialManager.getCredential(activity, request)
    }

    suspend operator fun invoke(response: GetCredentialResponse): Result<Unit> = runCatching {
        val credential = response.credential as? CustomCredential
            ?: throw Exception("Invalid credential type")

        if (credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            throw Exception("Invalid credential type")
        }

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        val idToken = googleIdTokenCredential.idToken

        Log.d("GoogleSignInUseCase", "ID Token: $idToken")

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()

        val user = authResult.user ?: throw Exception("Authentication failed")

        Log.d("GoogleSignInUseCase", "User: ${user.uid}")


        // Check if it's a new user
        if (authResult.additionalUserInfo?.isNewUser == true) {
            userRepository.createUser(user.uid, user.displayName ?: "", user.email ?: "")
        }

        authRepository.setUserAuthenticated(true)
    }
}