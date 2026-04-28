package org.example.project.presentation.screen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.example.project.domain.Priority
import org.example.project.presentation.component.InfoCard
import org.example.project.presentation.component.LoadingCard
import org.example.project.presentation.component.PriorityColor.getColor
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
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle() // This line expects Flow<T> OR StateFlow<T> ::-> Flow<RequestState<T>>
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val priorityFilter by viewModel.priorityFilter.collectAsStateWithLifecycle()

    var dropDownMenuOpened by remember { mutableStateOf(false) }
    var searchBarOpened by remember { mutableStateOf(false) }


    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    AnimatedContent(
                        targetState = searchBarOpened,
                    ) { isOpened ->
                        if (isOpened) {
                            TextField(
                                modifier = Modifier.height(56.dp),
                                value = searchQuery,
                                onValueChange = { textChanged ->
                                    viewModel.updateSearchQuery(textChanged)

                                },
                                placeholder = { Text(text = "Search...") },
                                shape = RoundedCornerShape(99.dp),
                                textStyle = TextStyle(
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                ),
                                singleLine = true,
                            )

                        } else {
                            Text(text = "To Do")
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(Resource.Icon.HAMBURGER_MENU),
                            contentDescription = "Hamburger menu icon"
                        )
                    }
                },
                actions = { // SearchAppBar
                    AnimatedContent(
                        targetState = searchBarOpened,
                    ) { isOpened ->
                        if (isOpened) {
                            IconButton(
                                onClick = {
                                    searchBarOpened = false
                                    viewModel.updateSearchQuery("")
                                }
                            ) {
                                Icon(
                                    painter = painterResource(Resource.Icon.CLOSE),
                                    contentDescription = "Close icon",
                                )
                            }
                        } else {
                            Row{

                                Box {
                                    Box (
                                        contentAlignment = Alignment.TopEnd
                                    ){
                                        IconButton(onClick = { dropDownMenuOpened = true }) {
                                            // IconButton size by default is 48dp by default
                                            Icon(
                                                painter = painterResource(Resource.Icon.SORT),
                                                contentDescription = "Sort icon",
                                            )
                                        }
                                        // The circle dot
                                        if(priorityFilter != Priority.NONE) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .offset(x = (-6).dp,  y = 6.dp) // moving the dot position closer to the icon.
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.errorContainer)
                                            )
                                        } else {
                                            Box{}
                                        }
                                    }
                                    DropdownMenu(
                                        expanded = dropDownMenuOpened,
                                        shape = RoundedCornerShape(12.dp),
                                        onDismissRequest = { dropDownMenuOpened = false },
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    ) {
                                        Priority.entries.forEach { priority ->
                                            DropdownMenuItem(
                                                modifier = Modifier
                                                    .background(
                                                        if (priorityFilter == priority && priorityFilter != Priority.NONE) MaterialTheme.colorScheme.outlineVariant
                                                        else  MaterialTheme.colorScheme.surfaceVariant
                                                    ),
                                                text = { Text(text = priority.name)},
                                                leadingIcon = {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(16.dp)
                                                            .clip(CircleShape)
                                                            .background(priority.getColor())
                                                    )
                                                },
                                                onClick = {
                                                    dropDownMenuOpened = false
                                                    viewModel.updatePriorityFilter(priority)
                                                }
                                            )
                                        }
                                    }
                                }
                                IconButton(onClick = { searchBarOpened = true }) {
                                    Icon(
                                        painter = painterResource(Resource.Icon.SEARCH),
                                        contentDescription = "Search icon",
                                    )
                                }
                            }
                        }
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

