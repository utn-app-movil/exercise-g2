package cr.ac.utn.taskmanager.data.memory

import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository

class TaskMemoryManager: ITaskRepository {
    private val tasks = mutableListOf<Task>()
    override fun addTask(task: Task){
        tasks.add(task)
    }

    override fun deleteTask(id: String) {
        tasks.removeIf { it.id.trim() == id.trim() }
    }

    override fun addTaskAt(task: Task, index: Int) {
        tasks.add(index.coerceIn(0, tasks.size), task)
    }

    override fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index >= 0) tasks[index] = task else tasks.add(task)
    }

    override fun getTasks() = tasks

    override fun getById(id: String): Task? {
        val result = tasks.filter { it.id.trim() == id.trim() }
        return if(result.any()) result[0] else null
    }
}