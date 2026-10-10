package com.learning.dashboard.presentation.detail

import com.learning.dashboard.domain.model.Course
import com.learning.dashboard.domain.model.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CourseDetailViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val initialCourse = Course(
        id = 1,
        title = "Python Programming",
        instructor = "John Smith",
        lessons = listOf(
            Lesson(order = 1, title = "Introduction", isCompleted = true),
            Lesson(order = 2, title = "Variables & Data Types", isCompleted = true),
            Lesson(order = 3, title = "Functions", isCompleted = false),
            Lesson(order = 4, title = "OOP", isCompleted = false)
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `marking a lesson complete recalculates course progress immediately`() = runTest {
        val repository = FakeCourseRepository(listOf(initialCourse))
        val viewModel = CourseDetailViewModel(repository, courseId = 1)
        dispatcher.scheduler.advanceUntilIdle()

        val before = viewModel.uiState.value as CourseDetailUiState.Success
        assertEquals(50, before.course.progress)

        viewModel.setLessonCompleted(lessonOrder = 3, isCompleted = true)
        dispatcher.scheduler.advanceUntilIdle()

        val after = viewModel.uiState.value as CourseDetailUiState.Success
        assertTrue(after.course.lessons.first { it.order == 3 }.isCompleted)
        assertEquals(75, after.course.progress)
    }
}
