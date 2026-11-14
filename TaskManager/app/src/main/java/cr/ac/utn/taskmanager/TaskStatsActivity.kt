package cr.ac.utn.taskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskStatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_stats)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvTotalTasks = findViewById<TextView>(R.id.tvTotalTasks)
        val tvPending = findViewById<TextView>(R.id.tvPending)
        val tvInProgress = findViewById<TextView>(R.id.tvInProgress)
        val tvCompleted = findViewById<TextView>(R.id.tvCompleted)
        val tvCancelled = findViewById<TextView>(R.id.tvCancelled)
        val tvPercentage = findViewById<TextView>(R.id.tvPercentage)
        val progressCompleted = findViewById<ProgressBar>(R.id.progressCompleted)
        val btnBackMain = findViewById<Button>(R.id.btnBackMain)

        val tasks = TaskViewModel.getTasks()
        val stats = TaskViewModel.getTaskStats()

        val total = tasks.count()
        val pending = stats[TaskStatus.PENDING] ?: 0
        val inProgress = stats[TaskStatus.IN_PROGRESS] ?: 0
        val completed = stats[TaskStatus.COMPLETED] ?: 0
        val cancelled = stats[TaskStatus.CANCELLED] ?: 0

        val percentage = if (total > 0) {
            completed * 100 / total
        } else {
            0
        }

        // Mostrar estadísticas utilizando strings.xml
        tvTotalTasks.text = getString(R.string.statsTotal) + " " + total
        tvPending.text = getString(R.string.statsPending) + " " + pending
        tvInProgress.text = getString(R.string.statsInProgress) + " " + inProgress
        tvCompleted.text = getString(R.string.statsCompleted) + " " + completed
        tvCancelled.text = getString(R.string.statsCancelled) + " " + cancelled

        // Mostrar porcentaje de tareas completadas
        tvPercentage.text = getString(R.string. statsPercentage) + " " + percentage + "%"

        // Actualizar barra de progreso
        progressCompleted.progress = percentage

        // Regresar al menú principal
        btnBackMain.setOnClickListener {
            finish()
        }
    }
}
