package com.learning.dashboard.data.remote

data class LessonDto(
    val order: Int,
    val title: String,
    val isCompleted: Boolean
)

data class CourseDto(
    val id: Int,
    val title: String,
    val instructor: String,
    val lessons: List<LessonDto>
)
