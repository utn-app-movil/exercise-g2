package cr.ac.utn.taskmanager

import android.content.Intent
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

    private lateinit var tvTotal: TextView
    private lateinit var tvPending: TextView
    private lateinit var tvInProgress: TextView
    private lateinit var tvCompleted: TextView
    private lateinit var tvCancelled: TextView
    private lateinit var tvProgress: TextView
    private lateinit var pbCompleted: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_stats)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvTotal = findViewById(R.id.tvTotal_Stats)
        tvPending = findViewById(R.id.tvPending_Stats)
        tvInProgress = findViewById(R.id.tvInProgress_Stats)
        tvCompleted = findViewById(R.id.tvCompleted_Stats)
        tvCancelled = findViewById(R.id.tvCancelled_Stats)
        tvProgress = findViewById(R.id.tvProgress_Stats)
        pbCompleted = findViewById(R.id.pbCompleted_Stats)

        findViewById<Button>(R.id.btnBack_Stats).setOnClickListener {
            goToMainMenu()
        }

        showStats()
    }

    private fun showStats() {
        val percentage = TaskViewModel.getCompletedPercentage()

        tvTotal.text = TaskViewModel.getTotalTasks().toString()
        tvPending.text = TaskViewModel.countByStatus(TaskStatus.PENDING).toString()
        tvInProgress.text = TaskViewModel.countByStatus(TaskStatus.IN_PROGRESS).toString()
        tvCompleted.text = TaskViewModel.countByStatus(TaskStatus.COMPLETED).toString()
        tvCancelled.text = TaskViewModel.countByStatus(TaskStatus.CANCELLED).toString()

        pbCompleted.progress = percentage
        tvProgress.text = getString(R.string.TextStatsProgress, percentage)
    }

    private fun goToMainMenu() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }
}