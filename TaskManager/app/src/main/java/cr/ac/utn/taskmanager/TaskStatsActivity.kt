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

    private lateinit var tvTotal: TextView
    private lateinit var tvPending: TextView
    private lateinit var tvInProgress: TextView
    private lateinit var tvCompleted: TextView
    private lateinit var tvCancelled: TextView
    private lateinit var tvCompletedPercent: TextView
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

        tvTotal = findViewById<TextView>(R.id.tvTotal_Stats)
        tvPending = findViewById<TextView>(R.id.tvPending_Stats)
        tvInProgress = findViewById<TextView>(R.id.tvInProgress_Stats)
        tvCompleted = findViewById<TextView>(R.id.tvCompleted_Stats)
        tvCancelled = findViewById<TextView>(R.id.tvCancelled_Stats)
        tvCompletedPercent = findViewById<TextView>(R.id.tvCompletedPercent_Stats)
        pbCompleted = findViewById<ProgressBar>(R.id.pbCompleted_Stats)

        loadStats()

        val btnBack = findViewById<Button>(R.id.btnBack_Stats)
        btnBack.setOnClickListener(View.OnClickListener{ view ->
            finish()
        })
    }

    private fun loadStats(){
        tvTotal.text = TaskViewModel.getTotalTasks().toString()
        tvPending.text = TaskViewModel.getTotalTasksByStatus(TaskStatus.PENDING).toString()
        tvInProgress.text = TaskViewModel.getTotalTasksByStatus(TaskStatus.IN_PROGRESS).toString()
        tvCompleted.text = TaskViewModel.getTotalTasksByStatus(TaskStatus.COMPLETED).toString()
        tvCancelled.text = TaskViewModel.getTotalTasksByStatus(TaskStatus.CANCELLED).toString()

        val completedPercentage = TaskViewModel.getCompletedPercentage()
        tvCompletedPercent.text = "$completedPercentage%"
        pbCompleted.progress = completedPercentage
    }
}