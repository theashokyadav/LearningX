package com.learning.dashboard.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.dashboard.domain.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseDetailViewModel(
    private val repository: CourseRepository,
    private val courseId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<CourseDetailUiState>(CourseDetailUiState.Loading)
    val uiState: StateFlow<CourseDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeCourses().collect { courses ->
                val course = courses.find { it.id == courseId }
                _uiState.value = if (course != null) {
                    CourseDetailUiState.Success(course)
                } else {
                    CourseDetailUiState.Error("Course not found")
                }
            }
        }
    }

    /**
     * Writes through the repository only; progress is recomputed from the
     * updated lesson list the next time it flows back from local storage.
     */
    fun setLessonCompleted(lessonOrder: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.setLessonCompleted(courseId, lessonOrder, isCompleted)
        }
    }
}
