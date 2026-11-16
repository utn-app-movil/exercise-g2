package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository

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

        fun saveTask(task: Task){
            repository.addTask(task)
        }

        fun updateTask(task: Task){
            repository.updateTask(task)
        }

        fun removeTask(id: String){
            repository.deleteTask(id)
        }

        fun getTotalTasks(): Int {
            return getTasks().size
        }

        fun countByStatus(status: TaskStatus): Int {
            return getTasks().count { it.status == status }
        }

        fun getCompletedPercentage(): Int {
            val total = getTotalTasks()
            return if (total == 0) 0 else countByStatus(TaskStatus.COMPLETED) * 100 / total
        }

        fun filterTasks(text: String, status: TaskStatus?): List<Task> {
            return getTasks().filter { task ->
                (status == null || task.status == status) &&
                        task.title.contains(text.trim(), ignoreCase = true)
            }
        }

    }

}