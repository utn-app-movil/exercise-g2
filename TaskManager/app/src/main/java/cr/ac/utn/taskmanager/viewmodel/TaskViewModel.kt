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
            try {
                if (repository.getById(task.id)== null)
                    repository.addTask(task)
                else
                    throw Exception("The task is duplicated. " +
                            "Please add a new one.")
            }catch (e: Exception){
                throw e
            }
        }

        fun updateTask(task: Task){
            try {
                if (repository.getById(task.id) != null)
                    repository.updateTask(task)
                else
                    throw Exception("It's not possible to update the task. " +
                            "because it does not exist.")
            }catch (e: Exception){
                throw e
            }
        }

        fun removeTask(id: String){
            try {
                if (repository.getById(id) != null)
                    repository.deleteTask(id)
                else
                    throw Exception("It's not possible to delete the task. " +
                            "because it does not exist.")
            }catch (e: Exception){
                throw e
            }
        }

        fun getTaskCount(): Int{
            return repository.getTasks().count()
        }

        fun getTaskCountByStatus(status: TaskStatus): Int{
            return repository.getTasks().count { it.status == status }
        }

        fun getCompletedPercentage(): Int{
            val total = getTaskCount()
            return if (total == 0) 0
                else getTaskCountByStatus(TaskStatus.COMPLETED) * 100 / total
        }

        fun getFilteredTasks(text: String, status: TaskStatus?): List<Task>{
            val query = text.trim()
            return repository.getTasks().filter {
                (status == null || it.status == status) &&
                (it.title.contains(query, true) || it.description.contains(query, true)
                    || it.id.contains(query, true) || it.assignedTo.contains(query, true))
            }
        }
    }

}