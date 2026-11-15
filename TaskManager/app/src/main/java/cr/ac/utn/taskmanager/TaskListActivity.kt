package cr.ac.utn.taskmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.taskmanager.domain.model.Task
import cr.ac.utn.taskmanager.util.EXTRA_MESSAGE_ID
import cr.ac.utn.taskmanager.util.util
import cr.ac.utn.taskmanager.viewmodel.TaskViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar

class TaskListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var recycler: RecyclerView
    private lateinit var txtSearch: EditText
    private lateinit var spStatus: Spinner

    private var currentTasks: List<Task> = listOf()

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
        txtSearch = findViewById(R.id.txtSearch_List)
        spStatus = findViewById(R.id.spStatus_List)

        recycler.layoutManager = LinearLayoutManager(applicationContext)

        val options = arrayOf("TODAS", "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED")
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatus.adapter = statusAdapter

        txtSearch.addTextChangedListener {
            filterTasks(txtSearch.text.toString(), spStatus.selectedItem.toString())
        }

        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                filterTasks(txtSearch.text.toString(), spStatus.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        filterTasks("", "TODAS")

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = currentTasks[viewHolder.bindingAdapterPosition]
                TaskViewModel.removeTask(task.id)
                filterTasks(txtSearch.text.toString(), spStatus.selectedItem.toString())

                Snackbar.make(recycler, R.string.TextDeleteSuccess, Snackbar.LENGTH_LONG)
                    .setAction(R.string.TextSnackbar) {
                        TaskViewModel.saveTask(task)
                        filterTasks(txtSearch.text.toString(), spStatus.selectedItem.toString())
                    }.show()
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recycler)
    }

    private fun filterTasks(text: String, status: String) {
        val filtered = TaskViewModel.getTasks().filter { task ->
            val matchText = task.title.contains(text.trim(), ignoreCase = true)
            val matchStatus = status == "TODAS" || task.status.name == status
            matchText && matchStatus
        }
        currentTasks = filtered
        recycler.adapter = TaskListAdapter(filtered, this)
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }
}