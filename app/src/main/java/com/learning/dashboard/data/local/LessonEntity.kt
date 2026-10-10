package com.learning.dashboard.data.local

import androidx.room.Entity

@Entity(tableName = "lessons", primaryKeys = ["courseId", "lessonOrder"])
data class LessonEntity(
    val courseId: Int,
    val lessonOrder: Int,
    val title: String,
    val isCompleted: Boolean
)
