package cr.ac.utn.taskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel
import cr.ac.utn.taskmanager.domain.model.TaskStatus

class TaskStatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_stats)

        // Obtener las tareas
        val tareas = TaskViewModel.getTasks()

        // Contar todas las tareas
        val total = tareas.count()

        // Contar las tareas por estado
        val pendientes = tareas.count { it.status == TaskStatus.PENDING }
        val enProgreso = tareas.count { it.status == TaskStatus.IN_PROGRESS }
        val completadas = tareas.filter {
            it.status == TaskStatus.COMPLETED
        }.count()
        val canceladas = tareas.count { it.status == TaskStatus.CANCELLED }

        // Calcular el porcentaje de tareas completadas
        var porcentaje = 0

        if (total > 0) {
            porcentaje = completadas * 100 / total
        }

        // Mostrar las estadísticas
        findViewById<TextView>(R.id.txtTotal).text =
            "Total de tareas: $total"

        findViewById<TextView>(R.id.txtPendientes).text =
            "Pendientes: $pendientes"

        findViewById<TextView>(R.id.txtEnProgreso).text =
            "En progreso: $enProgreso"

        findViewById<TextView>(R.id.txtCompletadas).text =
            "Completadas: $completadas"

        findViewById<TextView>(R.id.txtCanceladas).text =
            "Canceladas: $canceladas"

        findViewById<TextView>(R.id.txtPorcentaje).text =
            "Tareas completadas: $porcentaje%"

        // Actualizar la barra de progreso
        val barra = findViewById<ProgressBar>(R.id.progressTareas)
        barra.progress = porcentaje

        // Botón para volver al menú principal
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
