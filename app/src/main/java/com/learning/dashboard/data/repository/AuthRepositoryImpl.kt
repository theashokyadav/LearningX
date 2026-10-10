package com.learning.dashboard.data.repository

import com.learning.dashboard.data.remote.MockAuthService
import com.learning.dashboard.domain.model.User
import com.learning.dashboard.domain.repository.AuthRepository

class AuthRepositoryImpl(private val api: MockAuthService) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<User> =
        api.login(email, password)
}
