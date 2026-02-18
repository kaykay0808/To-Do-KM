package org.example.project.domain

import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// model class -> container that holds information your app cares about.
@OptIn(ExperimentalUuidApi::class)
data class ToDoTask(
    val id: String = Uuid.random().toHexString(),
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val priority: Priority

)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}
