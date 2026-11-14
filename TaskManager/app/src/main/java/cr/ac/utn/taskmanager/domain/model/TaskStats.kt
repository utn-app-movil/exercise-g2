package cr.ac.utn.taskmanager.domain.model

data class TaskStats(
    val total: Int,
    val pending: Int,
    val inProgress: Int,
    val completed: Int,
    val cancelled: Int
) {
    val completedPercent: Int
        get() = if (total == 0) 0 else completed * 100 / total
}
