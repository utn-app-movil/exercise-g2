package cr.ac.utn.taskmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var svSearch: SearchView
    private lateinit var spStatusFilter: Spinner
    private var currentSearchQuery: String = ""
    private var currentStatusFilter: String = "TODAS"
    private var displayedTasks: MutableList<Task> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        svSearch = findViewById(R.id.svSearch_TaskList)
        spStatusFilter = findViewById(R.id.spStatusFilter_TaskList)

        setupFilterSpinner()
        setupRecyclerView()
        setupSearchAndFilterListeners()
        setupSwipeToDelete()
    }

    override fun onResume() {
        super.onResume()
        applyFilters()
    }

    private fun setupFilterSpinner() {
        val options = mutableListOf(getString(R.string.option_all_statuses))
        TaskStatus.entries.forEach { options.add(it.name) }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatusFilter.adapter = adapter
    }

    private fun setupRecyclerView() {
        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)
        displayedTasks = TaskViewModel.getTasks().toMutableList()
        taskAdapter = TaskListAdapter(displayedTasks, this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = taskAdapter
    }

    private fun setupSearchAndFilterListeners() {
        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                currentSearchQuery = query ?: ""
                applyFilters()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentSearchQuery = newText ?: ""
                applyFilters()
                return true
            }
        })

        spStatusFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentStatusFilter = parent?.getItemAtPosition(position).toString()
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun applyFilters() {
        displayedTasks = TaskViewModel.getTasksFiltered(currentSearchQuery, currentStatusFilter).toMutableList()
        taskAdapter.updateTasks(displayedTasks)
    }

    private fun setupSwipeToDelete() {
        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position in displayedTasks.indices) {
                    val deletedTask = displayedTasks[position]
                    TaskViewModel.removeTask(deletedTask.id)
                    applyFilters()

                    val mainView = findViewById<View>(R.id.main)
                    Snackbar.make(mainView, getString(R.string.msg_task_deleted), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.action_undo)) {
                            TaskViewModel.saveTask(deletedTask)
                            applyFilters()
                        }
                        .show()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
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