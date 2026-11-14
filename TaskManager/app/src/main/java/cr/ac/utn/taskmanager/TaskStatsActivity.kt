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
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskStatsActivity : AppCompatActivity() {

    private lateinit var tvTotal: TextView
    private lateinit var tvPending: TextView
    private lateinit var tvInProgress: TextView
    private lateinit var tvCompleted: TextView
    private lateinit var tvCancelled: TextView
    private lateinit var tvPercent: TextView
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
        tvPercent = findViewById(R.id.tvPercent_Stats)
        pbCompleted = findViewById(R.id.pbCompleted_Stats)

        findViewById<Button>(R.id.btnBackMain_Stats).setOnClickListener {
            goToMainMenu()
        }
    }

    override fun onResume() {
        super.onResume()
        showStats()
    }

    private fun showStats() {
        val stats = TaskViewModel.getTaskStats()
        tvTotal.text = stats.total.toString()
        tvPending.text = getString(R.string.TextStatsPending, stats.pending)
        tvInProgress.text = getString(R.string.TextStatsInProgress, stats.inProgress)
        tvCompleted.text = getString(R.string.TextStatsCompleted, stats.completed)
        tvCancelled.text = getString(R.string.TextStatsCancelled, stats.cancelled)
        pbCompleted.progress = stats.completedPercent
        tvPercent.text = getString(R.string.TextStatsPercent, stats.completedPercent)
    }

    private fun goToMainMenu() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }
}