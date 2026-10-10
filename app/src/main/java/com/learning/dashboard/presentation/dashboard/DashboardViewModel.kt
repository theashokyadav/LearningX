package com.learning.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.dashboard.domain.repository.CourseRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: CourseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    init {
        loadCourses()
    }

    /**
     * Decides the initial state exactly as before (cache-first, refresh, fall back to
     * cache offline, error only when there's nothing cached). Once that initial state
     * has data, [observeCourses] takes over so later changes (e.g. a lesson completed
     * on the detail screen) are reflected here too, without a fresh load being needed.
     */
    fun loadCourses() {
        observeJob?.cancel()
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            val cached = repository.getCachedCourses()

            val hasData = try {
                repository.refreshCourses()
                true
            } catch (error: Exception) {
                if (cached.isNotEmpty()) {
                    true
                } else {
                    _uiState.value = DashboardUiState.Error(error.message ?: "Unable to load courses")
                    false
                }
            }

            if (hasData) {
                observeCourses()
            }
        }
    }

    private fun observeCourses() {
        observeJob = viewModelScope.launch {
            repository.observeCourses().collect { courses ->
                _uiState.value = if (courses.isEmpty()) {
                    DashboardUiState.Empty
                } else {
                    DashboardUiState.Success(courses)
                }
            }
        }
    }

    fun retry() = loadCourses()
}
