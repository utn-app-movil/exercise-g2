package cr.ac.utn.taskmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import cr.ac.utn.taskmanager.util.util

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val btnMessage1: Button =
            findViewById(R.id.btnMessage1_main)

        btnMessage1.setOnClickListener {
            Toast.makeText(
                this,
                getString(R.string.TextMessage1),
                Toast.LENGTH_LONG
            ).show()
        }

        val clmain =
            findViewById<ConstraintLayout>(R.id.main)

        class SnackbarClick : View.OnClickListener {
            override fun onClick(p0: View?) {
                Snackbar.make(
                    clmain,
                    "Cancelled action",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        val btnMessage2: Button =
            findViewById(R.id.btnMessage2_main)

        btnMessage2.setOnClickListener {
            val mySnackbar = Snackbar.make(
                clmain,
                R.string.TextMessage2,
                Snackbar.LENGTH_LONG
            )

            mySnackbar.setAction(
                R.string.TextSnackbar,
                SnackbarClick()
            )

            mySnackbar.show()
        }

        val btnOpenScreen: Button =
            findViewById(R.id.btnOpenScreen_main)

        btnOpenScreen.setOnClickListener {
            util.openActivity(
                this,
                SecondActivity::class.java
            )
        }

        val btnOpenTask: Button =
            findViewById(R.id.btnTask_main)

        btnOpenTask.setOnClickListener {
            util.openActivity(
                this,
                TaskActivity::class.java
            )
        }

        val btnOpenTaskList: Button =
            findViewById(R.id.btnTaskList_main)

        btnOpenTaskList.setOnClickListener {
            util.openActivity(
                this,
                TaskListActivity::class.java
            )
        }

        val btnTaskStats: Button =
            findViewById(R.id.btnTaskStats_main)

        btnTaskStats.setOnClickListener {
            val intent = Intent(
                this,
                TaskStatsActivity::class.java
            )

            startActivity(intent)
        }
    }
}

