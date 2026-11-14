package cr.ac.utn.taskmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskStatsActivity : AppCompatActivity() {

    private lateinit var tvTotal: TextView
    private lateinit var tvPending: TextView
    private lateinit var tvInProgress: TextView
    private lateinit var tvCompleted: TextView
    private lateinit var tvCancelled: TextView
    private lateinit var tvPercentage: TextView
    private lateinit var progressCompleted: ProgressBar
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_stats)

        tvTotal = findViewById(R.id.tvTotalTasks)
        tvPending = findViewById(R.id.tvPending)
        tvInProgress = findViewById(R.id.tvInProgress)
        tvCompleted = findViewById(R.id.tvCompleted)
        tvCancelled = findViewById(R.id.tvCancelled)
        tvPercentage = findViewById(R.id.tvPercentage)
        progressCompleted = findViewById(R.id.progressCompleted)
        btnBack = findViewById(R.id.btnBackMain)

        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadStatistics()
    }

    private fun loadStatistics() {
        val stats = TaskViewModel.getTaskStatistics()

        tvTotal.text = stats.total.toString()
        tvPending.text = stats.pending.toString()
        tvInProgress.text = stats.inProgress.toString()
        tvCompleted.text = stats.completed.toString()
        tvCancelled.text = stats.cancelled.toString()

        tvPercentage.text = getString(
            R.string.stats_percentage_format,
            stats.percentage
        )

        progressCompleted.max = 100
        progressCompleted.progress = stats.percentage
    }
}

