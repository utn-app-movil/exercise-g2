package cr.ac.utn.taskmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var recycler: RecyclerView
    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var svSearch: SearchView
    private lateinit var spStatusFilter: Spinner
    private lateinit var tvEmpty: TextView

    private var searchText: String = ""
    private var selectedStatus: TaskStatus? = null // null = TODAS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recycler = findViewById(R.id.rvTasks_List)
        svSearch = findViewById(R.id.svSearch_List)
        spStatusFilter = findViewById(R.id.spStatusFilter_List)
        tvEmpty = findViewById(R.id.tvEmpty_List)

        taskAdapter = TaskListAdapter(TaskViewModel.getTasks(), this)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.adapter = taskAdapter

        setupSearch()
        setupStatusFilter()
        setupSwipeToDelete()
    }

    override fun onResume() {
        super.onResume()
        // Refresca la lista al volver de editar/eliminar en TaskActivity
        applyFilters()
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }

    // ---------- Fase 2: busqueda y filtro combinado ----------

    private fun setupSearch() {
        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                searchText = newText.orEmpty()
                applyFilters()
                return true
            }
        })
    }

    private fun setupStatusFilter() {
        val options = listOf(getString(R.string.TextAllStatuses)) +
                TaskStatus.entries.map { it.name }
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatusFilter.adapter = adapter

        spStatusFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                selectedStatus = if (position == 0) null else TaskStatus.entries[position - 1]
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun applyFilters() {
        val filtered = TaskViewModel.filterTasks(searchText, selectedStatus)
        taskAdapter.updateTasks(filtered)
        tvEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    // ---------- Fase 3: Swipe to delete + Undo ----------

    private fun setupSwipeToDelete() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteWithUndo(viewHolder.bindingAdapterPosition)
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recycler)
    }

    private fun deleteWithUndo(position: Int) {
        if (position == RecyclerView.NO_POSITION) return

        val task = taskAdapter.getTaskAt(position)
        // Posicion en la lista completa, para restaurar en el mismo lugar
        val originalIndex = TaskViewModel.getTasks().indexOf(task)

        TaskViewModel.removeTask(task.id)
        applyFilters()

        Snackbar.make(recycler,
            getString(R.string.TextTaskDeleted, task.title),
            Snackbar.LENGTH_LONG)
            .setAction(R.string.TextUndoUpper) {
                TaskViewModel.restoreTask(task, originalIndex)
                applyFilters()
            }
            .show()
    }
}