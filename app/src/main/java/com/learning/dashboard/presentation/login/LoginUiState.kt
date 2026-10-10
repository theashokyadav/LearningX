package com.learning.dashboard.presentation.login

data class LoginUiState(
    val email: String = "test@learning.com",
    val password: String = "password123",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)
