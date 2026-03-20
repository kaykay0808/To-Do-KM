package org.example.project.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import org.example.project.data.ToDoRepository
import org.example.project.domain.ToDoTask
import org.example.project.util.RequestState

class HomeViewModel(
    private val repository: ToDoRepository
): ViewModel() {
    val allTasks = repository.readAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // if the app goes to the background or we go to a different screens it will stop collecting after 5 seconds.
            initialValue = RequestState.Loading
        )

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
}