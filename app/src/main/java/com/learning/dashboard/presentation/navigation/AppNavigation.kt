package com.learning.dashboard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.learning.dashboard.LearningDashboardApp
import com.learning.dashboard.presentation.dashboard.DashboardScreen
import com.learning.dashboard.presentation.dashboard.DashboardViewModel
import com.learning.dashboard.presentation.detail.CourseDetailScreen
import com.learning.dashboard.presentation.detail.CourseDetailViewModel
import com.learning.dashboard.presentation.login.LoginScreen
import com.learning.dashboard.presentation.login.LoginViewModel

private object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val COURSE_DETAIL = "course/{courseId}"
    const val COURSE_ID_ARG = "courseId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val container = (LocalContext.current.applicationContext as LearningDashboardApp).container

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            val viewModel: LoginViewModel = viewModel(
                factory = viewModelFactory {
                    initializer { LoginViewModel(container.authRepository, container.validateLoginInputUseCase) }
                }
            )
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            val viewModel: DashboardViewModel = viewModel(
                factory = viewModelFactory {
                    initializer { DashboardViewModel(container.courseRepository) }
                }
            )
            DashboardScreen(
                viewModel = viewModel,
                onCourseSelected = { courseId ->
                    navController.navigate("course/$courseId")
                }
            )
        }

        composable(
            route = Routes.COURSE_DETAIL,
            arguments = listOf(navArgument(Routes.COURSE_ID_ARG) { type = NavType.IntType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt(Routes.COURSE_ID_ARG) ?: -1
            val viewModel: CourseDetailViewModel = viewModel(
                key = "course_detail_$courseId",
                factory = viewModelFactory {
                    initializer { CourseDetailViewModel(container.courseRepository, courseId) }
                }
            )
            CourseDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
