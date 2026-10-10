package com.learning.dashboard.presentation.dashboard

import com.learning.dashboard.domain.model.Course

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(val courses: List<Course>) : DashboardUiState
    data object Empty : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
