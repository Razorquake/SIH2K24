package com.razorquake.sih2k24.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razorquake.sih2k24.domain.usecases.auth.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    init {
        viewModelScope.launch {
            authUseCases.getAuthStateUseCase().collect { isAuthenticated ->
                _authState.value = if (isAuthenticated) AuthState.Authenticated else AuthState.Unauthenticated
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authUseCases.loginUseCase(email, password)
                .onSuccess { _authState.value = AuthState.Authenticated }
                .onFailure { _authState.value = AuthState.Error(it.message ?: "Login failed") }
        }
    }

    fun signup(username: String, email: String, password: String) {
        viewModelScope.launch {
            authUseCases.signUpUseCase(username, email, password)
                .onSuccess { _authState.value = AuthState.Authenticated }
                .onFailure { _authState.value = AuthState.Error(it.message ?: "Signup failed") }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authUseCases.logoutUseCase()
                .onSuccess { _authState.value = AuthState.Unauthenticated }
                .onFailure { _authState.value = AuthState.Error(it.message ?: "Logout failed") }
        }
    }
}

sealed class AuthState {
    data object Initial : AuthState()
    data object Authenticated : AuthState()
    data object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}