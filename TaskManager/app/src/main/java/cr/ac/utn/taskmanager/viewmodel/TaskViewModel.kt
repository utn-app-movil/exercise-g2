package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository

class TaskViewModel : ViewModel() {
    companion object {
        private var repository: ITaskRepository = TaskMemoryManager()

        fun getTasks(): List<Task> {
            return repository.getTasks()
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

        fun getTotalTasksCount(): Int {
            return repository.getTasks().size
        }

        fun getTasksCountByStatus(status: TaskStatus): Int {
            return repository.getTasks().count { it.status == status }
        }

        fun getCompletionPercentage(): Int {
            val total = getTotalTasksCount()
            if (total == 0) return 0
            val completed = getTasksCountByStatus(TaskStatus.COMPLETED)
            return (completed * 100) / total
        }

        fun getTasksFiltered(query: String, statusFilter: String): List<Task> {
            val q = query.trim().lowercase()
            return repository.getTasks().filter { task ->
                val matchesQuery = q.isEmpty() ||
                        task.title.lowercase().contains(q) ||
                        task.description.lowercase().contains(q) ||
                        task.assignedTo.lowercase().contains(q)

                val matchesStatus = statusFilter.equals("TODAS", ignoreCase = true) ||
                        task.status.name.equals(statusFilter, ignoreCase = true)

                matchesQuery && matchesStatus
            }
        }
    }
}