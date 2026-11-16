package cr.ac.utn.taskmanager.data.memory

import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskPriority
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.domain.repository.ITaskRepository
import java.time.LocalDate

class TaskMemoryManager: ITaskRepository {
    private val tasks = mutableListOf<Task>(
        Task(
            id = "1",
            title = "Diseñar UI de la App",
            description = "Crear pantallas en XML para la gestión de tareas",
            dueDate = LocalDate.now().plusDays(3),
            creationDate = LocalDate.now(),
            status = TaskStatus.IN_PROGRESS,
            assignedTo = "María López",
            priority = TaskPriority.HIGH,
            notes = "Revisar guías de diseño de Android"
        ),
        Task(
            id = "2",
            title = "Depurar Errores en TaskManager",
            description = "Corregir los 8 errores intencionales del proyecto",
            dueDate = LocalDate.now().plusDays(1),
            creationDate = LocalDate.now(),
            status = TaskStatus.COMPLETED,
            assignedTo = "Carlos Gómez",
            priority = TaskPriority.CRITICAL,
            notes = "Usar modo Debug y Logcat"
        ),
        Task(
            id = "3",
            title = "Implementar Pantalla de Estadísticas",
            description = "Crear TaskStatsActivity y ViewModel de soporte",
            dueDate = LocalDate.now().plusDays(5),
            creationDate = LocalDate.now(),
            status = TaskStatus.PENDING,
            assignedTo = "Ana Rodríguez",
            priority = TaskPriority.MEDIUM,
            notes = "Calcular totales con count y filter"
        ),
        Task(
            id = "4",
            title = "Configurar Filtros en TaskList",
            description = "Agregar SearchView y Spinner para filtrar por estado",
            dueDate = LocalDate.now().plusDays(4),
            creationDate = LocalDate.now(),
            status = TaskStatus.CANCELLED,
            assignedTo = "Pedro Sánchez",
            priority = TaskPriority.LOW,
            notes = "Filtrar en tiempo real"
        )
    )

    override fun addTask(task: Task){
        tasks.add(task)
    }

    override fun deleteTask(id: String) {
        tasks.removeIf { it.id == id }
    }

    override fun updateTask(task: Task) {
        deleteTask(task.id)
        addTask(task)
    }

    override fun getTasks() = tasks

    override fun getById(id: String): Task? {
        val result = tasks.filter { it.id == id }
        return if(result.any()) result[0] else null
    }
}