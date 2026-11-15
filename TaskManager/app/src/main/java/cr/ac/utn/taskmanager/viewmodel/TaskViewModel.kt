package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository
import cr.ac.utn.taskmanager.domain.model.TaskStats
import cr.ac.utn.taskmanager.domain.model.TaskStatus


class TaskViewModel: ViewModel() {
    companion object{
        private var repository: ITaskRepository
        init {
            repository = TaskMemoryManager()
        }

        fun getTasks(): List<Task>{
            return repository.getTasks()
        }

        fun getTaskById(id: String): Task?{
            return repository.getById(id)
        }

        fun getStats(): TaskStats{
            val tasks = repository.getTasks()
            val total = tasks.size
            val completed = tasks.count { it.status == TaskStatus.COMPLETED }
            return TaskStats(
                total,
                tasks.count { it.status == TaskStatus.PENDING },
                tasks.count { it.status == TaskStatus.IN_PROGRESS },
                completed,
                tasks.count { it.status == TaskStatus.CANCELLED },
                if (total == 0) 0 else completed * 100 / total
            )
        }

        fun filterTasks(text: String, status: TaskStatus?): List<Task>{
            val query = text.trim()
            return repository.getTasks().filter { task ->
                (status == null || task.status == status)
                        && (task.title.contains(query, ignoreCase = true)
                        || task.description.contains(query, ignoreCase = true))
            }
        }

        fun saveTask(task: Task){
            if (task.status != TaskStatus.PENDING)
                throw Exception("A new task must be created with PENDING status.")

            if (repository.getById(task.id) == null)
                repository.addTask(task)
            else
                throw Exception("The task is duplicated. Please add a new one.")
        }

        fun updateTask(task: Task){
            repository.updateTask(task)
        }

        fun removeTask(id: String){
            if (repository.getById(id) != null)
                repository.deleteTask(id)
            else
                throw Exception("It's not possible to delete the task. because it does not exist.")
        }
    }

}