
package cr.ac.utn.taskmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // Obtener las tareas registradas
        val tareas = TaskViewModel.getTasks()

        // Configurar la lista
        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)
        val taskAdapter = TaskListAdapter(tareas, this)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = taskAdapter

        // Obtener el buscador y el Spinner
        val edtBuscar = findViewById<EditText>(R.id.edtBuscar)
        val spnEstado = findViewById<Spinner>(R.id.spnEstado)

        // Opciones del Spinner
        val estados = arrayOf(
            "TODAS",
            "PENDING",
            "IN_PROGRESS",
            "COMPLETED",
            "CANCELLED"
        )

        val adapterEstados = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            estados
        )

        adapterEstados.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spnEstado.adapter = adapterEstados

        // Función para filtrar las tareas
        fun filtrarTareas() {

            val texto = edtBuscar.text.toString()
            val estado = spnEstado.selectedItem.toString()

            val tareasFiltradas = tareas.filter { tarea ->

                val coincideTexto = tarea.title.contains(
                    texto,
                    ignoreCase = true
                )

                val coincideEstado =
                    estado == "TODAS" || tarea.status.name == estado

                coincideTexto && coincideEstado
            }

            taskAdapter.actualizarLista(tareasFiltradas)
        }

        // Buscar mientras se escribe
        edtBuscar.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                filtrarTareas()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Filtrar cuando cambia el estado
        spnEstado.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    filtrarTareas()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    override fun onItemClick(task: Task) {
        util.openActivity(
            this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID,
            task.id
        )
    }
}
