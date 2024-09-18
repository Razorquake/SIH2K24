package com.razorquake.sih2k24

import androidx.compose.material3.Scaffold
import com.razorquake.sih2k24.presentation.auth.AuthViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.razorquake.sih2k24.presentation.LoginScreen
import com.razorquake.sih2k24.presentation.auth.AuthState
import com.razorquake.sih2k24.presentation.sign_up.SignUpScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Route.Auth.route,
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()
    Scaffold {

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            navigation(
                startDestination = Route.LoginScreen.route,
                route = Route.Auth.route
            ) {
                composable(Route.LoginScreen.route) {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate(Route.Main.route) {
                                popUpTo(Route.Auth.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToSignUp = {
                            navController.navigate(Route.SignUp.route)
                        },
                        viewModel = authViewModel
                    )
                }
                composable(Route.SignUp.route) {
                    SignUpScreen(
                        onSignUpSuccess = {
                            navController.navigate(Route.Main.route) {
                                popUpTo(Route.Auth.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToLogin = {
                            navController.popBackStack()
                        },
                        viewModel = authViewModel
                    )
                }
            }

            composable(Route.Main.route) {
                Navigator(
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Route.Auth.route) {
                            popUpTo(Route.Main.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }

    // Handle authentication state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(Route.Main.route) {
                    popUpTo(Route.Auth.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
            is AuthState.Unauthenticated -> {
                navController.navigate(Route.Auth.route) {
                    popUpTo(Route.Main.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
            else -> {} // Do nothing for Initial and Error states
        }
    }
}