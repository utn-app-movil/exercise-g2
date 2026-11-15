package cr.ac.utn.taskmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
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

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var txtSearch: EditText
    private lateinit var spStatus: Spinner
    private lateinit var recycler: RecyclerView
    private lateinit var taskAdapter: TaskListAdapter

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
        spStatus = findViewById(R.id.spStatus_List)
        recycler = findViewById<RecyclerView>(R.id.rvTasks_List)

        setupSpinner()

        taskAdapter = TaskListAdapter(TaskViewModel.getTasks(),
            this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter

        txtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilters()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        setupSwipeToDelete()
    }

    override fun onResume() {
        super.onResume()
        applyFilters()
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }

    private fun setupSpinner(){
        val options = listOf(getString(R.string.TextAll)) + TaskStatus.entries.map { it.name }
        val statusAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            options
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatus.adapter = statusAdapter
    }

    private fun applyFilters(){
        val position = spStatus.selectedItemPosition
        val status = if (position > 0) TaskStatus.entries[position - 1] else null
        taskAdapter.updateTasks(
            TaskViewModel.filterTasks(txtSearch.text.toString(), status))
    }

    private fun setupSwipeToDelete(){
        val callback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION){
                    deleteTask(taskAdapter.getTaskAt(position))
                }
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recycler)
    }

    private fun deleteTask(task: Task){
        try {
            TaskViewModel.removeTask(task.id)
            Snackbar.make(recycler, R.string.TextTaskDeleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.TextSnackbar, View.OnClickListener{ view ->
                    TaskViewModel.saveTask(task)
                    applyFilters()
                })
                .show()
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(),
                Toast.LENGTH_LONG).show()
        }
        applyFilters()
    }
}