package com.razorquake.sih2k24.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.razorquake.sih2k24.domain.models.User
import com.razorquake.sih2k24.domain.usecases.user.GetUserInfoUseCase
import com.razorquake.sih2k24.domain.usecases.user.UpdateUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState

    init {
        loadUserInfo()
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                getUserInfoUseCase(userId)
                    .onSuccess { user -> _userState.value = UserState.Loaded(user) }
                    .onFailure { error -> _userState.value = UserState.Error(error.message ?: "Failed to load user info") }
            } else {
                _userState.value = UserState.Error("User not authenticated")
            }
        }
    }

    fun updateUserInfo(updates: Map<String, Any>) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                updateUserInfoUseCase(userId, updates)
                    .onSuccess { loadUserInfo() }
                    .onFailure { error -> _userState.value = UserState.Error(error.message ?: "Failed to update user info") }
            } else {
                _userState.value = UserState.Error("User not authenticated")
            }
        }
    }
}

sealed class UserState {
    object Loading : UserState()
    data class Loaded(val user: User) : UserState()
    data class Error(val message: String) : UserState()
}