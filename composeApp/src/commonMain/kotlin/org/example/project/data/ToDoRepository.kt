package org.example.project.data

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.ToDoTask
import org.example.project.util.RequestState
import kotlin.coroutines.CoroutineContext

// Defines which operations exist. -> Any data source must support these operations.
interface ToDoRepository {
    fun createTask(task: ToDoTask): RequestState<Unit> // the operation was successful or not
    fun updateTask(task: ToDoTask): RequestState<Unit>
    fun readSelectedTask(taskId: String): RequestState<ToDoTask>
    fun readAllTasks(/*context: CoroutineContext*/): Flow<RequestState<List<ToDoTask>>>
    fun removeTask(taskId: String): RequestState<Unit>
}


/**
 * repository.readAllTasks()
 *     .collect { state ->
 *         // update UI automatically when tasks change
 *     }
 * */