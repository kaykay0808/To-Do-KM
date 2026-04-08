package org.example.project.presentation.screen.task

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.example.project.data.ToDoRepository
import org.example.project.domain.Priority
import org.example.project.domain.ToDoTask
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class TaskUiState(
    val id: String? = null, // This will decide whether to update or create new task
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.LOW,
    val error: String? = null
)

class TaskViewModel(
    private val repository: ToDoRepository,
    // private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // _taskUiState → private, only ViewModel can change it
    private var _taskUiState: MutableState<TaskUiState> =
        mutableStateOf(TaskUiState())
    // uiState → public, TaskScreen can only READ it
    val uiState: State<TaskUiState> = _taskUiState

    fun loadData(taskId: String?) {
        if (taskId != null) {
            // editing existing task → load it from repository
            val existingTask = repository.readSelectedTask(taskId)
            if (existingTask.isSuccess()) {
                _taskUiState.value = TaskUiState(
                    id = taskId,
                    title = existingTask.getSuccessData().title,
                    description = existingTask.getSuccessData().description,
                    priority = existingTask.getSuccessData().priority
                )
            }
        } else {
            // creating new task → reset to empty
            _taskUiState.value = TaskUiState()
        }
    }

    // Every time user types in the text field:
    fun updateTitle(title: String) {
        _taskUiState.value = _taskUiState.value.copy(title = title) // copy current state but replace title
    }

    fun updateDescription(description: String) {
        _taskUiState.value = _taskUiState.value.copy(description = description)
    }


    fun updatePriority(priority: Priority) {
        _taskUiState.value = _taskUiState.value.copy(priority = priority)
    }

    //  saveTask — create or update
    @OptIn(ExperimentalUuidApi::class)
    fun saveTask(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val uiStateData = _taskUiState.value

        val task = ToDoTask(
            id = uiStateData.id ?: Uuid.random().toHexString(),
            title = uiStateData.title,
            description = uiStateData.description,
            priority = uiStateData.priority
        )

        val result = if (uiStateData.id != null) {
            repository.updateTask(task) // id exists = editing
        } else {
            repository.createTask(task) // no id = creating
        }
        if (result.isSuccess()) {
            onSuccess() // fires callback → shows snackbar
        } else if (result.isError()) {
            onError(result.getErrorMessage()) // fires callback → shows error snackbar
        }
    }
}

/**
 * TaskScreen opens
 *     → LaunchedEffect calls viewModel.loadData(id)
 *         → loads existing task OR empty state
 *
 * User types in title
 *     → updateTitle fires
 *         → state updates
 *             → UI recomposes
 *
 * User taps Save
 *     → saveTask fires
 *         → creates/updates in repository
 *             → onSuccess → snackbar shows
 * */
