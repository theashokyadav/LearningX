package com.learning.dashboard.domain.usecase

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}

/** Pure input validation, kept out of the ViewModel so it stays independently testable. */
class ValidateLoginInputUseCase {

    operator fun invoke(email: String, password: String): ValidationResult {
        if (email.isBlank()) return ValidationResult.Invalid("Email is required")
        if (!EMAIL_REGEX.matches(email)) return ValidationResult.Invalid("Enter a valid email address")
        if (password.isBlank()) return ValidationResult.Invalid("Password is required")
        if (password.length < 6) return ValidationResult.Invalid("Password must be at least 6 characters")
        return ValidationResult.Valid
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    }
}
