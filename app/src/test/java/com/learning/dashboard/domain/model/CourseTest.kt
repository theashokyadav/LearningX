package com.learning.dashboard.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CourseTest {

    private fun lesson(order: Int, completed: Boolean) =
        Lesson(order = order, title = "Lesson $order", isCompleted = completed)

    @Test
    fun `progress is zero when course has no lessons`() {
        val course = Course(id = 1, title = "Empty", instructor = "N/A", lessons = emptyList())

        assertEquals(0, course.progress)
    }

    @Test
    fun `progress is zero when no lessons are completed`() {
        val course = Course(
            id = 1,
            title = "Python",
            instructor = "John Smith",
            lessons = listOf(lesson(1, false), lesson(2, false), lesson(3, false), lesson(4, false))
        )

        assertEquals(0, course.progress)
    }

    @Test
    fun `progress is 100 when all lessons are completed`() {
        val course = Course(
            id = 1,
            title = "Python",
            instructor = "John Smith",
            lessons = listOf(lesson(1, true), lesson(2, true))
        )

        assertEquals(100, course.progress)
    }

    @Test
    fun `progress reflects the fraction of completed lessons`() {
        // 13 of 20 lessons completed, matching the dashboard's sample "65% complete" course.
        val lessons = (1..20).map { lesson(it, completed = it <= 13) }
        val course = Course(id = 1, title = "Python", instructor = "John Smith", lessons = lessons)

        assertEquals(65, course.progress)
    }

    @Test
    fun `completing one more lesson recalculates progress immediately`() {
        val lessons = (1..4).map { lesson(it, completed = it <= 1) }
        val course = Course(id = 1, title = "Course", instructor = "Someone", lessons = lessons)
        assertEquals(25, course.progress)

        val updated = course.copy(
            lessons = course.lessons.map {
                if (it.order == 2) it.copy(isCompleted = true) else it
            }
        )

        assertEquals(50, updated.progress)
    }
}
