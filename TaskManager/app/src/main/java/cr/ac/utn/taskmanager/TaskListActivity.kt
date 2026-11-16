package cr.ac.utn.taskmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
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

class TaskListActivity : AppCompatActivity(),
    OnItemClickListener {

    private lateinit var recycler: RecyclerView
    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var searchTasks: SearchView
    private lateinit var spFilterStatus: Spinner
    private lateinit var tvNoTasks: TextView

    private var searchText = ""
    private var selectedStatus: TaskStatus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)

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

        recycler = findViewById(R.id.rvTasks_List)
        searchTasks = findViewById(R.id.searchTasks)
        spFilterStatus = findViewById(R.id.spFilterStatus)
        tvNoTasks = findViewById(R.id.tvNoTasks)

        taskAdapter = TaskListAdapter(
            TaskViewModel.getTasks().toList(),
            this
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = taskAdapter

        setupSearch()
        setupStatusFilter()
        setupSwipeToDelete()
    }

    private fun setupSearch() {

        searchTasks.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(
                    query: String?
                ): Boolean {
                    return false
                }

                override fun onQueryTextChange(
                    newText: String?
                ): Boolean {

                    searchText = newText.orEmpty()
                    filterTasks()

                    return true
                }
            }
        )
    }

    private fun setupStatusFilter() {

        val statusOptions = listOf(
            getString(R.string.task_filter_all),
            getString(R.string.task_filter_pending),
            getString(R.string.task_filter_in_progress),
            getString(R.string.task_filter_completed),
            getString(R.string.task_filter_cancelled)
        )

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statusOptions
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spFilterStatus.adapter = spinnerAdapter

        spFilterStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    selectedStatus = when (position) {
                        1 -> TaskStatus.PENDING
                        2 -> TaskStatus.IN_PROGRESS
                        3 -> TaskStatus.COMPLETED
                        4 -> TaskStatus.CANCELLED
                        else -> null
                    }

                    filterTasks()
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                    selectedStatus = null
                    filterTasks()
                }
            }
    }

    private fun filterTasks() {

        val tasks = TaskViewModel.getTasks()

        val filteredTasks = tasks.filter { task ->

            val matchesSearch =
                task.id.contains(
                    searchText,
                    ignoreCase = true
                ) ||
                        task.title.contains(
                            searchText,
                            ignoreCase = true
                        ) ||
                        task.description.contains(
                            searchText,
                            ignoreCase = true
                        )

            val matchesStatus =
                selectedStatus == null ||
                        task.status == selectedStatus

            matchesSearch && matchesStatus
        }

        taskAdapter.updateTasks(filteredTasks)

        tvNoTasks.visibility =
            if (filteredTasks.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
    }

    private fun setupSwipeToDelete() {

        val swipeCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
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

                    val position =
                        viewHolder.bindingAdapterPosition

                    if (position == RecyclerView.NO_POSITION) {
                        filterTasks()
                        return
                    }

                    val task = taskAdapter.getTaskAt(position)

                    if (task == null) {
                        filterTasks()
                        return
                    }

                    deleteTaskWithUndo(task)
                }
            }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)

        itemTouchHelper.attachToRecyclerView(recycler)
    }

    private fun deleteTaskWithUndo(task: Task) {

        try {
            TaskViewModel.removeTask(task.id)

            filterTasks()

            Snackbar.make(
                findViewById(R.id.main),
                getString(R.string.task_swipe_deleted),
                Snackbar.LENGTH_LONG
            )
                .setAction(
                    getString(R.string.task_swipe_undo)
                ) {
                    try {
                        TaskViewModel.saveTask(task)

                        filterTasks()

                        Toast.makeText(
                            this,
                            getString(R.string.task_swipe_restored),
                            Toast.LENGTH_SHORT
                        ).show()

                    } catch (e: Exception) {

                        Toast.makeText(
                            this,
                            e.message ?: getString(
                                R.string.task_swipe_error
                            ),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .show()

        } catch (e: Exception) {

            filterTasks()

            Toast.makeText(
                this,
                e.message ?: getString(
                    R.string.task_swipe_error
                ),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()

        if (::taskAdapter.isInitialized) {
            filterTasks()
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
