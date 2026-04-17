@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package org.example.project.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.example.project.presentation.component.InfoCard
import org.example.project.presentation.screen.home.HomeScreen
import org.example.project.presentation.screen.task.TaskScreen
import org.example.project.util.Resource
import org.koin.compose.koinInject

@Composable
actual fun NavGraph() {
    // NavDisplay defines our different destinations
    // val navigator = remember { Navigator() }
    val navigator = koinInject<Navigator>()

    // for landscape mode
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    // 👆 checks the current screen size
    // "am I on a phone, tablet, or foldable?"

    val directive = remember(key1 = windowAdaptiveInfo) {
        // Takes the screen info and calculates
        // "how should I arrange the layout?"
        // Phone  → show one screen at a time
        // Tablet → show two screens side by side
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
        //        👆 removes the gap between screens

    }

    val listDetailStrategy = rememberListDetailSceneStrategy<Any>(directive = directive)


    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        sceneStrategy = listDetailStrategy,
        entryProvider = entryProvider {
            entry<Screen.Home>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        InfoCard(
                            lightModeIcon = Resource.Image.PAINTING_LIGHT,
                            darkModeIcon = Resource.Image.PAINTING_DARK,
                            message = "Select an existing task or create a new one.",
                            containerColor = MaterialTheme.colorScheme.surfaceVariant

                        )
                    }
                )
            ) {
                HomeScreen(
                    navigateToTask = { taskId ->
                        navigator.navigateToTask(taskId)
                    }
                )
            }
            entry<Screen.Task>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                TaskScreen(
                    id = it.id,
                    navigateBack = { navigator.goBack() }
                )
            }
        }
    )
}