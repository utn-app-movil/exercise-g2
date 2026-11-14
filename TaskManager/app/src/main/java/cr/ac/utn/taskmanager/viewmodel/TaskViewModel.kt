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

        fun saveTask(task: Task){
            repository.addTask(task)
        }

        fun updateTask(task: Task){
            repository.updateTask(task)
        }

        fun removeTask(id: String){
            repository.deleteTask(id)
        }

        // Fase 1: totales calculados con count
        fun getTaskStats(): TaskStats {
            val tasks = repository.getTasks()
            return TaskStats(
                total = tasks.size,
                pending = tasks.count { it.status == TaskStatus.PENDING },
                inProgress = tasks.count { it.status == TaskStatus.IN_PROGRESS },
                completed = tasks.count { it.status == TaskStatus.COMPLETED },
                cancelled = tasks.count { it.status == TaskStatus.CANCELLED }
            )
        }

        // Fase 2: filtro combinado por texto y estado (status = null significa TODAS)
        fun filterTasks(query: String, status: TaskStatus? = null): List<Task> {
            val text = query.trim()
            return repository.getTasks().filter { task ->
                val matchesStatus = status == null || task.status == status
                val matchesText = text.isEmpty()
                        || task.title.contains(text, ignoreCase = true)
                        || task.description.contains(text, ignoreCase = true)
                        || task.assignedTo.contains(text, ignoreCase = true)
                        || task.id.contains(text, ignoreCase = true)
                matchesStatus && matchesText
            }
        }

        // Restaura una tarea eliminada en su posicion original (Undo)
        fun restoreTask(task: Task, index: Int){
            repository.addTaskAt(index, task)
        }
    }

}