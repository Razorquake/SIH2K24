package com.razorquake.sih2k24.data

object Validators {
    fun validateEmail(email: String): ValidationResult {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
        return when {
            email.isEmpty() -> ValidationResult(false, "Email cannot be empty")
            !email.matches(emailRegex) -> ValidationResult(false, "Invalid email format")
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(false, "Password cannot be empty")
            password.length < 8 -> ValidationResult(false, "Password must be at least 8 characters long")
            !password.any { it.isDigit() } -> ValidationResult(false, "Password must contain at least one digit")
            !password.any { it.isLetter() } -> ValidationResult(false, "Password must contain at least one letter")
            !password.any { !it.isLetterOrDigit() } -> ValidationResult(false, "Password must contain at least one special character")
            !password.any { it.isUpperCase() } -> ValidationResult(false, "Password must contain at least one uppercase letter")
            !password.any { it.isLowerCase() } -> ValidationResult(false, "Password must contain at least one lowercase letter")
            else -> ValidationResult(true)
        }
    }

    fun validateUsername(username: String): ValidationResult {
        return when {
            username.isEmpty() -> ValidationResult(false, "Username cannot be empty")
            username.length < 3 -> ValidationResult(false, "Username must be at least 3 characters long")
            else -> ValidationResult(true)
        }
    }
}

data class ValidationResult(val isValid: Boolean, val errorMessage: String? = null)