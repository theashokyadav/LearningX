package com.learning.dashboard.domain.repository

import com.learning.dashboard.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    /** Always backed by local storage, so it works with or without network. */
    fun observeCourses(): Flow<List<Course>>

    /** One-shot read of whatever is currently cached locally. */
    suspend fun getCachedCourses(): List<Course>

    /** Fetches from the remote source and overwrites the local cache on success. Throws on failure. */
    suspend fun refreshCourses()

    suspend fun setLessonCompleted(courseId: Int, lessonOrder: Int, isCompleted: Boolean)
}
