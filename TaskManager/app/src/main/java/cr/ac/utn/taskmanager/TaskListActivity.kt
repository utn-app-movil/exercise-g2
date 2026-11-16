package cr.ac.utn.taskmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
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
    private lateinit var txtSearch: EditText
    private lateinit var spFilterStatus: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtSearch = findViewById(R.id.txtSearch_TaskList)
        spFilterStatus = findViewById(R.id.spFilterStatus_TaskList)
        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)

        taskAdapter = TaskListAdapter(TaskViewModel.getTasks(), this)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.adapter = taskAdapter

        setupFilterSpinner()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val deletedTask = taskAdapter.getTaskAt(position)

                TaskViewModel.removeTask(deletedTask.id)
                filterTasks()

                val snackbar = Snackbar.make(recycler, R.string.TextDeleteSuccess, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.TextSnackbar) {
                    TaskViewModel.saveTask(deletedTask)
                    filterTasks()
                }
                snackbar.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)

        txtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterTasks()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        spFilterStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterTasks()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupFilterSpinner() {
        val filterOptions = mutableListOf("TODAS")
        TaskStatus.entries.forEach { filterOptions.add(it.name) }

        val statusAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            filterOptions
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spFilterStatus.adapter = statusAdapter
    }

    private fun filterTasks() {
        val query = txtSearch.text.toString().trim()
        val selectedStatus = spFilterStatus.selectedItem?.toString() ?: "TODAS"

        val allTasks = TaskViewModel.getTasks()
        val filteredTasks = allTasks.filter { task ->
            val matchesText = query.isEmpty() ||
                    task.title.contains(query, ignoreCase = true) ||
                    task.description.contains(query, ignoreCase = true) ||
                    task.assignedTo.contains(query, ignoreCase = true) ||
                    task.id.contains(query, ignoreCase = true)
            val matchesStatus = selectedStatus == "TODAS" || task.status.name == selectedStatus
            matchesText && matchesStatus
        }

        taskAdapter.updateTasks(filteredTasks)
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this, TaskActivity::class.java, EXTRA_MESSAGE_ID, task.id)
    }
}
