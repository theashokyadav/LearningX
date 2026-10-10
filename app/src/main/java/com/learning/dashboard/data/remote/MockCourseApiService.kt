package com.learning.dashboard.data.remote

import kotlinx.coroutines.delay
import java.io.IOException

/**
 * Stands in for a backend. Returns a fixed catalog after a simulated network delay,
 * but genuinely checks device connectivity so flipping airplane mode exercises the
 * same failure path a real API outage would.
 */
class MockCourseApiService(private val networkMonitor: NetworkMonitor) {

    suspend fun fetchCourses(): List<CourseDto> {
        delay(900)

        if (!networkMonitor.isOnline()) {
            throw IOException("No internet connection")
        }

        return CATALOG
    }

    companion object {
        private fun lessons(
            total: Int,
            completed: Int,
            namedTitles: List<String> = emptyList()
        ): List<LessonDto> = (1..total).map { order ->
            val title = namedTitles.getOrNull(order - 1) ?: "Lesson $order"
            LessonDto(order = order, title = title, isCompleted = order <= completed)
        }

        private val CATALOG = listOf(
            CourseDto(
                id = 1,
                title = "Python Programming",
                instructor = "John Smith",
                lessons = lessons(
                    total = 20,
                    completed = 13,
                    namedTitles = listOf(
                        "Introduction",
                        "Variables & Data Types",
                        "Functions",
                        "OOP"
                    )
                )
            ),
            CourseDto(
                id = 2,
                title = "Generative AI",
                instructor = "Sarah Williams",
                lessons = lessons(total = 16, completed = 6)
            ),
            CourseDto(
                id = 3,
                title = "Full Stack Development",
                instructor = "David Brown",
                lessons = lessons(total = 28, completed = 7)
            )
        )
    }
}
