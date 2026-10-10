package com.learning.dashboard.presentation.detail

import com.learning.dashboard.domain.model.Course
import com.learning.dashboard.domain.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** In-memory stand-in for [CourseRepository], scoped to this test module. */
class FakeCourseRepository(initialCourses: List<Course>) : CourseRepository {

    private val coursesFlow = MutableStateFlow(initialCourses)

    override fun observeCourses(): StateFlow<List<Course>> = coursesFlow

    override suspend fun getCachedCourses(): List<Course> = coursesFlow.value

    override suspend fun refreshCourses() {
        // No-op: tests drive state directly via setLessonCompleted.
    }

    override suspend fun setLessonCompleted(courseId: Int, lessonOrder: Int, isCompleted: Boolean) {
        coursesFlow.value = coursesFlow.value.map { course ->
            if (course.id != courseId) return@map course
            course.copy(
                lessons = course.lessons.map { lesson ->
                    if (lesson.order == lessonOrder) lesson.copy(isCompleted = isCompleted) else lesson
                }
            )
        }
    }
}
