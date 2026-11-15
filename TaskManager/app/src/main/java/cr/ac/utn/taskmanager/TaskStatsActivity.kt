
package cr.ac.utn.taskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskStatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_task_stats)

        val tvTotalTasks =
            findViewById<TextView>(R.id.tvTotalTasks)

        val tvPending =
            findViewById<TextView>(R.id.tvPending)

        val tvInProgress =
            findViewById<TextView>(R.id.tvInProgress)

        val tvCompleted =
            findViewById<TextView>(R.id.tvCompleted)

        val tvCancelled =
            findViewById<TextView>(R.id.tvCancelled)

        val tvPercentage =
            findViewById<TextView>(R.id.tvPercentage)

        val progressCompleted =
            findViewById<ProgressBar>(R.id.progressCompleted)

        val btnBackMain =
            findViewById<Button>(R.id.btnBackMain)

        val tasks = TaskViewModel.getTasks()

        val total = tasks.size

        val pending = tasks.count {
            it.status == TaskStatus.PENDING
        }

        val inProgress = tasks.count {
            it.status == TaskStatus.IN_PROGRESS
        }

        val completed = tasks.count {
            it.status == TaskStatus.COMPLETED
        }

        val cancelled = tasks.count {
            it.status == TaskStatus.CANCELLED
        }

        val percentage = if (total > 0) {
            (completed * 100) / total
        } else {
            0
        }

        tvTotalTasks.text = total.toString()

        tvPending.text = "Pendientes: $pending"

        tvInProgress.text = "En progreso: $inProgress"

        tvCompleted.text = "Completadas: $completed"

        tvCancelled.text = "Canceladas: $cancelled"

        tvPercentage.text =
            "Tareas completadas: $percentage%"

        progressCompleted.progress = percentage

        btnBackMain.setOnClickListener {
            finish()
        }
    }
}
