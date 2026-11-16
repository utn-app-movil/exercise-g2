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
    private lateinit var txtSearch: EditText
    private lateinit var spStatus: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtSearch = findViewById(R.id.txtSearch_List)
        spStatus = findViewById(R.id.spStatus_List)

        val recycler= findViewById<RecyclerView>(R.id.rvTasks_List)
        taskAdapter = TaskListAdapter(TaskViewModel.getTasks(),
            this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val deletedTask = taskAdapter.getTaskAt(position)

                TaskViewModel.removeTask(deletedTask.id)
                applyFilter()

                Snackbar.make(recycler, R.string.TextTaskDeleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.TextSnackbar) {
                        TaskViewModel.saveTask(deletedTask)
                        applyFilter()
                    }
                    .show()
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recycler)

        setupStatusSpinner()

        txtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilter()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupStatusSpinner() {
        val options = mutableListOf(getString(R.string.TextAll))
        options.addAll(TaskStatus.entries.map { it.name })

        val statusAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            options
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatus.adapter = statusAdapter

        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilter()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun applyFilter() {
        val position = spStatus.selectedItemPosition
        val status: TaskStatus? = if (position <= 0) null else TaskStatus.entries[position - 1]
        val filtered = TaskViewModel.filterTasks(txtSearch.text.toString(), status)
        taskAdapter.updateTasks(filtered)
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }
}