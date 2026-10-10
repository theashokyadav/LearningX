package com.learning.dashboard.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Transaction
    @Query("SELECT * FROM courses ORDER BY id")
    fun observeCoursesWithLessons(): Flow<List<CourseWithLessons>>

    @Transaction
    @Query("SELECT * FROM courses ORDER BY id")
    suspend fun getCoursesWithLessonsOnce(): List<CourseWithLessons>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Query("DELETE FROM courses")
    suspend fun clearCourses()

    @Query("DELETE FROM lessons")
    suspend fun clearLessons()

    @Transaction
    suspend fun replaceAll(courses: List<CourseEntity>, lessons: List<LessonEntity>) {
        clearLessons()
        clearCourses()
        insertCourses(courses)
        insertLessons(lessons)
    }

    @Query(
        "UPDATE lessons SET isCompleted = :isCompleted " +
            "WHERE courseId = :courseId AND lessonOrder = :lessonOrder"
    )
    suspend fun setLessonCompleted(courseId: Int, lessonOrder: Int, isCompleted: Boolean)
}
