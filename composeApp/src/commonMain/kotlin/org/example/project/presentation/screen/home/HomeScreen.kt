package org.example.project.presentation.screen.home

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.example.project.presentation.component.InfoCard
import org.example.project.presentation.component.LoadingCard
import org.example.project.presentation.component.TaskCard
import org.example.project.util.DisplayResult
import org.example.project.util.Resource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToTask: (String?) -> Unit
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle() // This line expects Flow<T> OR StateFlow<T> ::-> Flow<RequestState<T>>
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "To Do") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(Resource.Icon.HAMBURGER_MENU),
                            contentDescription = "Hamburger menu icon"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navigateToTask(null) },
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(Resource.Icon.ADD),
                    contentDescription = "Plus icon"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "New Task")
            }
        }

    ) { paddingValues ->
        allTasks.DisplayResult(
            modifier = Modifier.padding(paddingValues),
            onLoading = { LoadingCard() },
            onSuccess = { tasks ->
                if (tasks.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(all = 12.dp)
                    ) {
                        items(
                            items = tasks,
                            key = { it.id }
                        ) {
                            TaskCard(
                                task = it,
                                onClick = navigateToTask,
                                onComplete = {
                                    val isCompleted = !it.isCompleted // 👈 flip it
                                    val result = viewModel.markTaskAsCompleted(
                                        task = it.copy(isCompleted = isCompleted) // 👈 copy with new value
                                    )

                                    if (result.isSuccess()) {
                                        scope.launch {
                                            snackBarHostState.showSnackbar(
                                                message = if (isCompleted) "Task marked as completed!"
                                                else "Task marked as not completed!"
                                            )
                                        }
                                    }
                                },
                                onDelete = {
                                    val result = viewModel.removeTask(
                                        taskId = it.id
                                    )

                                    if (result.isSuccess()) {
                                        scope.launch {
                                            snackBarHostState.showSnackbar(
                                                message = "Task removed successfully!"
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                } else {
                    InfoCard(message = "Empty Tasks.")
                }
            },
            onError = { message ->
                InfoCard(
                    message = message,
                    lightModeIcon = Resource.Image.WARNING_LIGHT,
                    darkModeIcon = Resource.Image.WARNING_DARK
                )
            },
            transitionSpec = slideInVertically() + fadeIn() togetherWith
                    slideOutVertically() + fadeOut()
        )
    }
}

