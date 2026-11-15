package cr.ac.utn.taskmanager.domain.model

data class TaskStats(
    val total: Int,
    val pending: Int,
    val inProgress: Int,
    val completed: Int,
    val cancelled: Int,
    val completedPercentage: Int
)
