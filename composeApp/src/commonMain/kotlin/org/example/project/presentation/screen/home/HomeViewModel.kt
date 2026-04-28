package org.example.project.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.example.project.data.ToDoRepository
import org.example.project.domain.Priority
import org.example.project.domain.ToDoTask
import org.example.project.util.RequestState

// ViewModel converts Flow → StateFlow
// readAllTasks() → gives a Flow
// stateIn(...) → converts it into a StateFlow
class HomeViewModel(
    private val repository: ToDoRepository
) : ViewModel() {
    // MutableState     → Compose UI state (lives in composables)
    // MutableStateFlow → ViewModel state  (lives in ViewModels, survives rotation)

    private var _priorityFilter = MutableStateFlow(Priority.NONE)
    val priorityFilter: StateFlow<Priority> = _priorityFilter// 👈 read only for UI (public)

    private var _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery// 👈 read only for UI (public)

    val allTasks = combine(
        repository.readAllTasks(), // 👈 flow 1: list of task
        _priorityFilter, // 👈 flow 2: filter
        _searchQuery // 👈 flow 3: search text

    ) { tasks, priority, query -> // 👈 runs when either changes
        when (tasks) {
            is RequestState.Success -> { // tasks loaded ✅
                // filter them
                val filteredTasks = tasks.data // 👈 the actual list of tasks
                    .let { list ->
                        if (priority == Priority.NONE)
                            list
                        else
                            list.filter { it.priority == priority }

                    }
                    .let { list ->
                        if (query.isBlank()) list
                        else list.filter {
                            it.title.lowercase().contains(query, ignoreCase = false) ||
                                    it.description.lowercase().contains(query, ignoreCase = false)
                        }
                    }
                    .sortedByDescending { it.priority.ordinal }
                RequestState.Success(filteredTasks)
            }

            else -> tasks // still loading or error → just pass through
        }
        // StateFlow = "live box of data that UI can observe"
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // if the app goes to the background or we go to a different screens it will stop collecting after 5 seconds.
        initialValue = RequestState.Loading
    )


    fun markTaskAsCompleted(task: ToDoTask): RequestState<Unit> {
        return repository.updateTask(task)
    }

    fun removeTask(taskId: String): RequestState<Unit> {
        return repository.removeTask(taskId)
    }

    fun updateSearchQuery(query: String) {
        // Update our field
        _searchQuery.value = query
    }

    fun updatePriorityFilter(priority: Priority) {
        // Update our field
        _priorityFilter.value = priority
    }
}

// Testing Error
/*val allTasks: StateFlow<RequestState<List<ToDoTask>>> = flowOf(RequestState.Error(message ="Fuck off"))
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // if the app goes to the background or we go to a different screens it will stop collecting after 5 seconds.
        initialValue = RequestState.Loading
    )*/

// Cleaner way to testing error
/*val allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(
    RequestState.Error("Fuck Off")
)*/

// Loading test
/*val allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(
    RequestState.Loading
)*/