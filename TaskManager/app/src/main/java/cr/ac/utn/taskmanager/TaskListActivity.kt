
package cr.ac.utn.taskmanager

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var taskAdapter: TaskListAdapter

    private var searchText = ""
    private var selectedStatus = "TODAS"

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

        val recycler =
            findViewById<RecyclerView>(R.id.rvTasks_List)

        val searchView =
            findViewById<SearchView>(R.id.searchTasks)

        val spinner =
            findViewById<Spinner>(R.id.spStatusFilter)

        taskAdapter = TaskListAdapter(
            TaskViewModel.getTasks().toMutableList(),
            this
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = taskAdapter
        setupSwipeToDelete(recycler)

        val statusOptions = listOf(
            "TODAS",
            "PENDING",
            "IN_PROGRESS",
            "COMPLETED",
            "CANCELLED"
        )

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statusOptions
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = spinnerAdapter

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchText = newText.orEmpty()
                    filterTasks()
                    return true
                }
            }
        )

        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedStatus = statusOptions[position]
                    filterTasks()
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }
    }

    private fun setupSwipeToDelete(recycler: RecyclerView) {

        val swipe = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
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
                val position = viewHolder.bindingAdapterPosition

                if (position == RecyclerView.NO_POSITION) return

                val task = taskAdapter.getTaskAt(position)

                TaskViewModel.removeTask(task.id)


                filterTasks()

                Snackbar.make(recycler, "Tarea eliminada", Snackbar.LENGTH_LONG)
                    .setAction("DESHACER") {
                        TaskViewModel.saveTask(task)
                        filterTasks()
                    }
                    .show()
            }
        }

        ItemTouchHelper(swipe).attachToRecyclerView(recycler)
    }


    private fun filterTasks() {

        val tasks = TaskViewModel.getTasks()

        val filteredTasks = tasks.filter { task ->

            val matchesText = task.title.contains(
                searchText,
                ignoreCase = true
            )

            val matchesStatus =
                selectedStatus == "TODAS" ||
                        task.status.name == selectedStatus

            matchesText && matchesStatus
        }

        taskAdapter.updateTasks(filteredTasks)
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
