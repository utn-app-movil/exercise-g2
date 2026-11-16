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

        val txtTotal = findViewById<TextView>(R.id.txtTotal_Stats)
        val txtPending = findViewById<TextView>(R.id.txtPending_Stats)
        val txtInProgress = findViewById<TextView>(R.id.txtInProgress_Stats)
        val txtCompleted = findViewById<TextView>(R.id.txtCompleted_Stats)
        val txtCancelled = findViewById<TextView>(R.id.txtCancelled_Stats)
        val pbCompleted = findViewById<ProgressBar>(R.id.pbCompleted_Stats)
        val btnBack = findViewById<Button>(R.id.btnBack_Stats)

        txtTotal.text = getString(R.string.TextTotalTasks) + " " + TaskViewModel.getTotalTasks()
        txtPending.text = getString(R.string.TextPending) + " " + TaskViewModel.countByStatus(TaskStatus.PENDING)
        txtInProgress.text = getString(R.string.TextInProgress) + " " + TaskViewModel.countByStatus(TaskStatus.IN_PROGRESS)
        txtCompleted.text = getString(R.string.TextCompleted) + " " + TaskViewModel.countByStatus(TaskStatus.COMPLETED)
        txtCancelled.text = getString(R.string.TextCancelled) + " " + TaskViewModel.countByStatus(TaskStatus.CANCELLED)
        pbCompleted.progress = TaskViewModel.getCompletedPercentage()

        btnBack.setOnClickListener {
            finish()
        }
    }
}