package com.learning.dashboard.data.repository

import com.learning.dashboard.data.local.CourseDao
import com.learning.dashboard.data.local.CourseEntity
import com.learning.dashboard.data.local.CourseWithLessons
import com.learning.dashboard.data.local.LessonEntity
import com.learning.dashboard.data.remote.MockCourseApiService
import com.learning.dashboard.domain.model.Course
import com.learning.dashboard.domain.model.Lesson
import com.learning.dashboard.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CourseRepositoryImpl(
    private val api: MockCourseApiService,
    private val dao: CourseDao
) : CourseRepository {

    override fun observeCourses(): Flow<List<Course>> =
        dao.observeCoursesWithLessons().map { it.toDomain() }

    override suspend fun getCachedCourses(): List<Course> =
        dao.getCoursesWithLessonsOnce().toDomain()

    override suspend fun refreshCourses() {
        val remoteCourses = api.fetchCourses()

        val courseEntities = remoteCourses.map { dto ->
            CourseEntity(id = dto.id, title = dto.title, instructor = dto.instructor)
        }
        val lessonEntities = remoteCourses.flatMap { dto ->
            dto.lessons.map { lesson ->
                LessonEntity(
                    courseId = dto.id,
                    lessonOrder = lesson.order,
                    title = lesson.title,
                    isCompleted = lesson.isCompleted
                )
            }
        }

        dao.replaceAll(courseEntities, lessonEntities)
    }

    override suspend fun setLessonCompleted(courseId: Int, lessonOrder: Int, isCompleted: Boolean) {
        dao.setLessonCompleted(courseId, lessonOrder, isCompleted)
    }

    private fun List<CourseWithLessons>.toDomain(): List<Course> = map { entry ->
        Course(
            id = entry.course.id,
            title = entry.course.title,
            instructor = entry.course.instructor,
            lessons = entry.lessons
                .sortedBy { it.lessonOrder }
                .map { Lesson(order = it.lessonOrder, title = it.title, isCompleted = it.isCompleted) }
        )
    }
}
