package com.learning.dashboard.data.remote

import com.learning.dashboard.domain.model.User
import kotlinx.coroutines.delay

/**
 * Stands in for a backend auth endpoint. Accepts one demo account so the
 * success and failure paths are both genuinely reachable from the UI.
 */
class MockAuthService {

    suspend fun login(email: String, password: String): Result<User> {
        delay(1000)

        return if (email.equals(DEMO_EMAIL, ignoreCase = true) && password == DEMO_PASSWORD) {
            Result.success(User(email = email))
        } else {
            Result.failure(InvalidCredentialsException())
        }
    }

    companion object {
        const val DEMO_EMAIL = "test@learning.com"
        const val DEMO_PASSWORD = "password123"
    }
}

class InvalidCredentialsException : Exception("Incorrect email or password")
