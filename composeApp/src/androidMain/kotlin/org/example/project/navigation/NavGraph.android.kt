package org.example.project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.example.project.presentation.screen.home.HomeScreen
import org.example.project.presentation.screen.task.TaskScreen
import org.koin.compose.koinInject

@Composable
actual fun NavGraph() {
    // NavDisplay defines our different destinations
    // val navigator = remember { Navigator() }
    val navigator = koinInject<Navigator>()

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryProvider = entryProvider {
            entry<Screen.Home> {
                HomeScreen(
                    navigateToTask = { taskId ->
                        navigator.navigateTo(Screen.Task(taskId))
                    }
                )
            }
            entry<Screen.Task> {
                TaskScreen(
                    id = it.id,
                    navigateBack = { navigator.goBack() }
                )
            }
        }
    )
}