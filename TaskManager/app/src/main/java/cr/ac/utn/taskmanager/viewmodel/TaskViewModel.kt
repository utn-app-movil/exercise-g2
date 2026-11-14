package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository

data class TaskStatistics(
    val total: Int,
    val pending: Int,
    val inProgress: Int,
    val completed: Int,
    val cancelled: Int,
    val percentage: Int
)

class TaskViewModel : ViewModel() {

    companion object {

        private var repository: ITaskRepository

        init {
            repository = TaskMemoryManager()
        }

        fun getTasks(): List<Task> {
            return repository.getTasks()
        }

        fun getTaskById(id: String): Task? {
            return repository.getById(id)
        }

        fun saveTask(task: Task) {
            if (repository.getById(task.id) == null) {
                repository.addTask(task)
            } else {
                throw Exception("The task already exists.")
            }
        }

        fun updateTask(task: Task) {
            if (repository.getById(task.id) != null) {
                repository.updateTask(task)
            } else {
                throw Exception("The task does not exist.")
            }
        }

        fun removeTask(id: String) {
            if (repository.getById(id) != null) {
                repository.deleteTask(id)
            } else {
                throw Exception("The task does not exist.")
            }
        }

        fun getTaskStatistics(): TaskStatistics {

            val tasks = getTasks()

            val total = tasks.count()

            val pending = tasks.count {
                it.status == TaskStatus.PENDING
            }

            val inProgress = tasks.count {
                it.status == TaskStatus.IN_PROGRESS
            }

            val completed = tasks.filter {
                it.status == TaskStatus.COMPLETED
            }.count()

            val cancelled = tasks.count {
                it.status == TaskStatus.CANCELLED
            }

            val percentage = if (total > 0) {
                (completed * 100) / total
            } else {
                0
            }

            return TaskStatistics(
                total,
                pending,
                inProgress,
                completed,
                cancelled,
                percentage
            )
        }
    }
}

