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
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
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

        // Configurar la lista de tareas
        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)

        val taskAdapter = TaskListAdapter(
            TaskViewModel.getTasks(),
            this
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = taskAdapter

        // Buscador y filtro por estado
        val edtBuscar = findViewById<EditText>(R.id.edtBuscar)
        val spnEstado = findViewById<Spinner>(R.id.spnEstado)

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

        // Filtrar las tareas
        fun filtrarTareas() {

            val texto = edtBuscar.text.toString()
            val estado = spnEstado.selectedItem.toString()

            val tareasFiltradas = TaskViewModel.getTasks().filter { tarea ->

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

        // Filtrar por estado
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

        // Fase 3: Eliminar tareas deslizando
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {

                // Obtener la tarea deslizada
                val posicion = viewHolder.bindingAdapterPosition

                if (posicion == RecyclerView.NO_POSITION) {
                    filtrarTareas()
                    return
                }

                val tarea = taskAdapter.obtenerTarea(posicion)

                // Eliminar la tarea
                TaskViewModel.removeTask(tarea.id)

                // Actualizar la lista
                filtrarTareas()

                // Mostrar opción para deshacer
                Snackbar.make(
                    recycler,
                    "Tarea eliminada",
                    Snackbar.LENGTH_LONG
                ).setAction("DESHACER") {

                    // Restaurar la tarea
                    TaskViewModel.saveTask(tarea)

                    // Actualizar nuevamente la lista
                    filtrarTareas()

                }.show()
            }
        }

        // Activar el gesto en el RecyclerView
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(recycler)
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
