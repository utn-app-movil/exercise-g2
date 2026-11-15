package cr.ac.utn.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository
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
            return repository.getTasks().size
        }

        fun getTasksByStatus(status: TaskStatus): Int {
            return repository.getTasks().count { task ->
                task.status == status
            }
        }

        fun getCompletedPercentage(): Int {
            val tasks = repository.getTasks()

            if (tasks.isEmpty()) {
                return 0
            }

            val completed = tasks.count {
                it.status == TaskStatus.COMPLETED
            }

            return (completed * 100) / tasks.size
        }

    }

}