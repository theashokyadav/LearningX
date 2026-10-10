package com.learning.dashboard.di

import android.content.Context
import com.learning.dashboard.data.local.AppDatabase
import com.learning.dashboard.data.remote.MockAuthService
import com.learning.dashboard.data.remote.MockCourseApiService
import com.learning.dashboard.data.remote.NetworkMonitor
import com.learning.dashboard.data.repository.AuthRepositoryImpl
import com.learning.dashboard.data.repository.CourseRepositoryImpl
import com.learning.dashboard.domain.repository.AuthRepository
import com.learning.dashboard.domain.repository.CourseRepository
import com.learning.dashboard.domain.usecase.ValidateLoginInputUseCase

/**
 * Hand-rolled dependency container. Scoped to the Application so every ViewModel
 * can be built from the same long-lived instances without a DI framework.
 */
class AppContainer(context: Context) {

    private val database = AppDatabase.getInstance(context)
    private val networkMonitor = NetworkMonitor(context)

    private val authApi = MockAuthService()
    private val courseApi = MockCourseApiService(networkMonitor)

    val authRepository: AuthRepository = AuthRepositoryImpl(authApi)
    val courseRepository: CourseRepository = CourseRepositoryImpl(courseApi, database.courseDao())

    val validateLoginInputUseCase = ValidateLoginInputUseCase()
}
