package org.example.project.data

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.example.project.domain.Priority
import org.example.project.domain.ToDoTask
import org.example.project.util.RequestState
import kotlin.coroutines.CoroutineContext

class FakeToDoRepository : ToDoRepository {
    private val tasks = mutableStateListOf<ToDoTask>()

    init {
        tasks.addAll(
            listOf(
                ToDoTask(
                    title = "Simple task 1",
                    description = "Some random task",
                    isCompleted = true,
                    priority = Priority.LOW
                ),
                ToDoTask(
                    title = "Simple task 2",
                    description = "Some random task 2",
                    isCompleted = false,
                    priority = Priority.HIGH
                ),
            )
        )
    }

    override fun createTask(task: ToDoTask): RequestState<Unit> {
        return try {
            val existingTask = tasks.find { it.id == task.id }
            if (existingTask != null) {
                RequestState.Error("Task with id ${task.id} already exists")
            } else {
                tasks.add(task)
                RequestState.Success(Unit)
            }
        } catch (e: Exception) {
            RequestState.Error("Failed to create a task: ${e.message}")
        }
    }

    override fun updateTask(task: ToDoTask): RequestState<Unit> {
        return try {
            val index = tasks.indexOfFirst { it.id == task.id }
            if (index != -1) {
                tasks[index] = task
                RequestState.Success(Unit)
            } else {
                RequestState.Error("Task with id ${task.id} not found")
            }
        } catch (e: Exception) {
            RequestState.Error("Failed to update a task: ${e.message}")
        }
    }

    override fun readSelectedTask(taskId: String): RequestState<ToDoTask> {
        return try {
            val existingTask = tasks.find { it.id == taskId }
            if (existingTask != null) {
                RequestState.Success(data = existingTask)
            } else {
                RequestState.Error("Task with id $taskId not found")
            }
        } catch (e: Exception) {
            RequestState.Error("Failed to read a selected task: ${e.message}")
        }
    }

    override fun readAllTasks(context: CoroutineContext): Flow<RequestState<List<ToDoTask>>> {
        return try {
            flowOf(RequestState.Success(tasks))
        } catch (e: Exception) {
            flowOf(RequestState.Error("Failed to read all tasks: ${e.message}"))
        }
    }

    override fun removeTask(taskId: String): RequestState<Unit> {
        return try {
            val taskToRemove = tasks.find { it.id == taskId }
            if (taskToRemove != null) {
                tasks.remove(taskToRemove)
                RequestState.Success(Unit)
                } else {
                RequestState.Error("Task with id $taskId not found")
            }
        } catch (e: Exception) {
            RequestState.Error("Failed to remove a task: ${e.message}")
        }
    }
}