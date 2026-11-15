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

        findViewById<TextView>(R.id.tvTotal_Stats).text =
            TaskViewModel.getTotalTasks().toString()

        findViewById<TextView>(R.id.tvPending_Stats).text =
            getString(R.string.TextStatsPending) + ": " + TaskViewModel.countByStatus(TaskStatus.PENDING)
        findViewById<TextView>(R.id.tvInProgress_Stats).text =
            getString(R.string.TextStatsInProgress) + ": " + TaskViewModel.countByStatus(TaskStatus.IN_PROGRESS)
        findViewById<TextView>(R.id.tvCompleted_Stats).text =
            getString(R.string.TextStatsCompleted) + ": " + TaskViewModel.countByStatus(TaskStatus.COMPLETED)
        findViewById<TextView>(R.id.tvCancelled_Stats).text =
            getString(R.string.TextStatsCancelled) + ": " + TaskViewModel.countByStatus(TaskStatus.CANCELLED)

        val percent = TaskViewModel.getCompletedPercentage()
        findViewById<ProgressBar>(R.id.pbCompleted_Stats).progress = percent
        findViewById<TextView>(R.id.tvPercent_Stats).text = "$percent%"

        findViewById<Button>(R.id.btnBack_Stats).setOnClickListener {
            finish()
        }
    }
}