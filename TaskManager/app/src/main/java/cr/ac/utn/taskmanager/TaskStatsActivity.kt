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

        val btnBack = findViewById<Button>(R.id.btnBack_Stats)
        btnBack.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        val total = TaskViewModel.getTaskCount()
        val pending = TaskViewModel.getTaskCountByStatus(TaskStatus.PENDING)
        val inProgress = TaskViewModel.getTaskCountByStatus(TaskStatus.IN_PROGRESS)
        val completed = TaskViewModel.getTaskCountByStatus(TaskStatus.COMPLETED)
        val cancelled = TaskViewModel.getTaskCountByStatus(TaskStatus.CANCELLED)
        val percentage = TaskViewModel.getCompletedPercentage()

        findViewById<TextView>(R.id.tvTotal_Stats).text = getString(R.string.TextTotalTasks, total)
        findViewById<TextView>(R.id.tvPending_Stats).text = getString(R.string.TextPendingTasks, pending)
        findViewById<TextView>(R.id.tvProgress_Stats).text = getString(R.string.TextProgressTasks, inProgress)
        findViewById<TextView>(R.id.tvCompleted_Stats).text = getString(R.string.TextCompletedTasks, completed)
        findViewById<TextView>(R.id.tvCancelled_Stats).text = getString(R.string.TextCancelledTasks, cancelled)
        findViewById<TextView>(R.id.tvPercentage_Stats).text = getString(R.string.TextCompletedPercentage, percentage)
        findViewById<ProgressBar>(R.id.pbCompleted_Stats).progress = percentage
    }
}
