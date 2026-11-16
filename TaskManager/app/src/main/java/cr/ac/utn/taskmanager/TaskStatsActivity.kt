package cr.ac.utn.taskmanager

import android.os.Bundle
import android.view.View
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

        val tvTotal = findViewById<TextView>(R.id.tvTotalTasks_Stats)
        val tvPending = findViewById<TextView>(R.id.tvPending_Stats)
        val tvInProgress = findViewById<TextView>(R.id.tvInProgress_Stats)
        val tvCompleted = findViewById<TextView>(R.id.tvCompleted_Stats)
        val tvCancelled = findViewById<TextView>(R.id.tvCancelled_Stats)
        val pbCompleted = findViewById<ProgressBar>(R.id.pbCompleted_Stats)
        val btnBack = findViewById<Button>(R.id.btnBack_Stats)

        val tasks = TaskViewModel.getTasks()
        val total = tasks.size
        val pending = tasks.count { it.status == TaskStatus.PENDING }
        val inProgress = tasks.filter { it.status == TaskStatus.IN_PROGRESS }.size
        val completed = tasks.count { it.status == TaskStatus.COMPLETED }
        val cancelled = tasks.filter { it.status == TaskStatus.CANCELLED }.size

        val percentage = if (total > 0) (completed * 100) / total else 0

        tvTotal.text = "${getString(R.string.textTotalTasks)} $total"
        tvPending.text = "${getString(R.string.textPendingTasks)} $pending"
        tvInProgress.text = "${getString(R.string.textInProgressTasks)} $inProgress"
        tvCompleted.text = "${getString(R.string.textCompletedTasks)} $completed"
        tvCancelled.text = "${getString(R.string.textCancelledTasks)} $cancelled"

        pbCompleted.progress = percentage

        btnBack.setOnClickListener(View.OnClickListener { view ->
            finish()
        })
    }
}
