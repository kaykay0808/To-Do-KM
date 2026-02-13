package org.example.project.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Navigator {
    // SnapshotStateList -> A list that Compose watches for changes. So when you add/remove screens recomposes UI
    val backStack: SnapshotStateList<Screen> = mutableStateListOf(Screen.Home)

    fun navigateTo(screen: Screen) {
        backStack.add(screen)
    }

    fun goBack() {
        // adjustment for later wrap it in a if block -> if (backStack.size > 1) {}
        backStack.removeLastOrNull()
    }
}