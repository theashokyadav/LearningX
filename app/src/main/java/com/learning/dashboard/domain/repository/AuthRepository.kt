package com.learning.dashboard.domain.repository

import com.learning.dashboard.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
}
