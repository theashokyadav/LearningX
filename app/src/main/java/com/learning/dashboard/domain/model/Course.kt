package com.learning.dashboard.domain.model

data class Course(
    val id: Int,
    val title: String,
    val instructor: String,
    val lessons: List<Lesson>
) {
    val lessonCount: Int get() = lessons.size

    /**
     * Progress is derived from lesson completion rather than stored directly,
     * so it can never drift out of sync with the lessons themselves.
     */
    val progress: Int get() =
        if (lessons.isEmpty()) 0
        else (lessons.count { it.isCompleted } * 100) / lessons.size
}
