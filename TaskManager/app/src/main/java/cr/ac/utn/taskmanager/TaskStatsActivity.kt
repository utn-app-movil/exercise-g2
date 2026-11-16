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
        btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        updateStatistics()
    }

    private fun updateStatistics() {
        val totalTasks = TaskViewModel.getTotalTasksCount()
        val pendingCount = TaskViewModel.getTasksCountByStatus(TaskStatus.PENDING)
        val inProgressCount = TaskViewModel.getTasksCountByStatus(TaskStatus.IN_PROGRESS)
        val completedCount = TaskViewModel.getTasksCountByStatus(TaskStatus.COMPLETED)
        val cancelledCount = TaskViewModel.getTasksCountByStatus(TaskStatus.CANCELLED)
        val completionPercentage = TaskViewModel.getCompletionPercentage()

        findViewById<TextView>(R.id.tvTotalTasks_Stats).text = totalTasks.toString()
        findViewById<TextView>(R.id.tvPendingCount_Stats).text = pendingCount.toString()
        findViewById<TextView>(R.id.tvInProgressCount_Stats).text = inProgressCount.toString()
        findViewById<TextView>(R.id.tvCompletedCount_Stats).text = completedCount.toString()
        findViewById<TextView>(R.id.tvCancelledCount_Stats).text = cancelledCount.toString()

        val progressBar = findViewById<ProgressBar>(R.id.pbCompletion_Stats)
        progressBar.progress = completionPercentage

        val tvPercentage = findViewById<TextView>(R.id.tvPercentage_Stats)
        tvPercentage.text = getString(R.string.percentage_format, completionPercentage)
    }
}