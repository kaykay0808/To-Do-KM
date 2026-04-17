package org.example.project.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Navigator {
    // SnapshotStateList -> A list that Compose watches for changes. So when you add/remove screens recomposes UI
    val backStack: SnapshotStateList<Screen> = mutableStateListOf(Screen.Home)

    fun navigateTo(screen: Screen) {
        backStack.add(screen)
    }

    // A special function for our task screen to not duplicate
    fun navigateToTask(taskId: String? = null) {
        if(backStack.lastOrNull() is Screen.Task) {
            // already on a task screen
            // just REPLACE it instead of adding
            backStack[backStack.lastIndex] = Screen.Task(taskId)
        } else {
            // not on a task screen yet
            // ADD it normally
            backStack.add(Screen.Task(id = taskId))

        }
    }

    fun goBack() {
        // adjustment for later wrap it in a if block -> if (backStack.size > 1) {}
        backStack.removeLastOrNull()
    }
}