package org.example.project.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    // 2 screens
    @Serializable
    object Home : Screen()
    @Serializable
    data class Task(val id: String? = null) : Screen()
}
