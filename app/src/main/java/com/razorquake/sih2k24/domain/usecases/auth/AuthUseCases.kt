package com.razorquake.sih2k24.domain.usecases.auth

data class AuthUseCases(
    val loginUseCase: LoginUseCase,
    val signUpUseCase: SignUpUseCase,
    val logoutUseCase: LogoutUseCase,
    val getAuthStateUseCase: GetAuthStateUseCase,
    val googleSignInUseCase: GoogleSignInUseCase
)
