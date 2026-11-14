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
import cr.ac.utn.taskmanager.data.memory.TaskMemoryManager
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.domain.model.TaskStatus
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel

class TaskListActivity : AppCompatActivity(), OnItemClickListener,
            TextWatcher, AdapterView.OnItemSelectedListener {
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

        txtSearch = findViewById<EditText>(R.id.txtSearch_List)
        spStatus = findViewById<Spinner>(R.id.spStatus_List)

        setupSpinner()

        val recycler= findViewById<RecyclerView>(R.id.rvTasks_List)
        taskAdapter = TaskListAdapter(TaskViewModel.getTasks(),
            this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter

        txtSearch.addTextChangedListener(this)
        spStatus.onItemSelectedListener = this
    }

    override fun onResume() {
        super.onResume()
        filterTasks()
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        filterTasks()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        filterTasks()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun setupSpinner() {
        val statusOptions = mutableListOf(getString(R.string.TextAll))
        statusOptions.addAll(TaskStatus.entries.map { it.name })

        val statusAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statusOptions
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatus.adapter = statusAdapter
    }

    private fun filterTasks(){
        val searchText = txtSearch.text.toString().trim()
        val selectedStatus = spStatus.selectedItem.toString()
        val status = if (selectedStatus == getString(R.string.TextAll)) null
                     else TaskStatus.valueOf(selectedStatus)

        taskAdapter.updateTasks(TaskViewModel.filterTasks(searchText, status))
    }
}