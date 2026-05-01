package org.example.project.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.example.project.TaskDatabase
import org.example.project.TaskTable
import org.example.project.domain.Priority
import org.example.project.domain.ToDoTask
import org.example.project.util.RequestState
import kotlin.coroutines.CoroutineContext
import kotlin.time.Clock

class ToDoRepositoryImplementation(
    databaseDriverFactory: DatabaseDriverFactory
) : ToDoRepository {
    private val database = TaskDatabase(
        driver = databaseDriverFactory.createDriver()
    )
    private val query = database.taskDatabaseQueries

    override fun createTask(task: ToDoTask): RequestState<Unit> {
        return try {
            query.insertTask(
                id = task.id,
                title = task.title,
                description = task.description,
                isCompleted = if (task.isCompleted) 1 else 0,
                priority = task.priority.name,
                created_at = Clock.System.now().toEpochMilliseconds(),
                updated_at = Clock.System.now().toEpochMilliseconds()

            )
            RequestState.Success(data = Unit)
        } catch (e: Exception) {
            RequestState.Error("${e.message}")
        }
    }

    override fun updateTask(task: ToDoTask): RequestState<Unit> {
        return try {
            query.updateTask(
                id = task.id,
                title = task.title,
                description = task.description,
                isCompleted = if (task.isCompleted) 1 else 0,
                priority = task.priority.name,
                updated_at = Clock.System.now().toEpochMilliseconds()
            )
            RequestState.Success(data = Unit)
        } catch (e: Exception) {
            RequestState.Error("${e.message}")
        }
    }

    override fun readSelectedTask(taskId: String): RequestState<ToDoTask> {
        return try {
            val task = query.selectTaskById(taskId)
                .executeAsOneOrNull()
            task?.let {
                RequestState.Success(data = task.convert())
            } ?: RequestState.Error("Task not found")
        } catch (e: Exception) {
            RequestState.Error("${e.message}")
        }
    }

    override fun readAllTasks(context: CoroutineContext): Flow<RequestState<List<ToDoTask>>> {
        return query.selectAllTasks()
            .asFlow()
            .mapToList(context)
            .map { task ->
                RequestState.Success(data = task.map { it.convert() })
            }
            .catch {
                RequestState.Error("${it.message}")
            }
    }

    override fun removeTask(taskId: String): RequestState<Unit> {
        return try {
            query.deleteTaskById(taskId)
            RequestState.Success(data = Unit)
        } catch (e: Exception) {
            RequestState.Error("${e.message}")
        }
    }
}

// Helper function
fun TaskTable.convert(): ToDoTask {
    return ToDoTask(
        id = this.id,
        title = this.title,
        description = this.description,
        isCompleted = this.isCompleted == 1L,
        priority = Priority.valueOf(this.priority),
        createdAt = this.created_at,
        updatedAt = this.updated_at
    )
}
