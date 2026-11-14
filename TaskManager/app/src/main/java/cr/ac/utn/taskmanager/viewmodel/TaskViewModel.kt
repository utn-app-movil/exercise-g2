
package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository

class TaskViewModel: ViewModel() {
    companion object {
        private var repository: ITaskRepository

        init {
            repository = TaskMemoryManager()
        }

        fun getTasks(): List<Task> {
            return repository.getTasks()
        }


        fun getTaskStats(): Map<TaskStatus, Int> {
            val tasks = getTasks()

            return mapOf(
                TaskStatus.PENDING to tasks.filter { it.status == TaskStatus.PENDING }.count(),
                TaskStatus.IN_PROGRESS to tasks.filter { it.status == TaskStatus.IN_PROGRESS }.count(),
                TaskStatus.COMPLETED to tasks.filter { it.status == TaskStatus.COMPLETED }.count(),
                TaskStatus.CANCELLED to tasks.filter { it.status == TaskStatus.CANCELLED }.count()
            )
        }


        fun getTaskById(id: String): Task? {
            return repository.getById(id)
        }

        fun saveTask(task: Task) {
            repository.addTask(task)
        }

        fun updateTask(task: Task) {
            repository.updateTask(task)
        }

        fun removeTask(id: String) {
            repository.deleteTask(id)
        }
    }
}
