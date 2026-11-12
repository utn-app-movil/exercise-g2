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

    private lateinit var recycler: RecyclerView
    private lateinit var taskAdapter: TaskListAdapter
    private lateinit var svSearch: SearchView
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

        recycler = findViewById<RecyclerView>(R.id.rvTasks_List)
        svSearch = findViewById<SearchView>(R.id.svSearch_List)
        spStatus = findViewById<Spinner>(R.id.spStatus_List)
        taskAdapter = TaskListAdapter(TaskViewModel.getTasks(), this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter

        val statusAdapter = ArrayAdapter.createFromResource(this,
            R.array.TaskStatusFilter, android.R.layout.simple_spinner_item)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatus.adapter = statusAdapter

        svSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterTasks()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTasks()
                return true
            }
        })

        spStatus.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                filterTasks()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                filterTasks()
            }
        }

        val callback = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    filterTasks()
                    return
                }
                val task = taskAdapter.getTask(position)
                TaskViewModel.removeTask(task.id)
                filterTasks()
                Snackbar.make(recycler, R.string.TextDeleteSuccess, Snackbar.LENGTH_LONG)
                    .setAction(R.string.TextSnackbar) {
                        TaskViewModel.saveTask(task)
                        filterTasks()
                    }.show()
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recycler)
    }

    override fun onResume() {
        super.onResume()
        if (::taskAdapter.isInitialized)
            filterTasks()
    }

    private fun filterTasks(){
        val status = if (spStatus.selectedItemPosition <= 0) null
            else TaskStatus.entries[spStatus.selectedItemPosition - 1]
        taskAdapter.updateTasks(TaskViewModel.getFilteredTasks(svSearch.query.toString(), status))
    }

    override fun onItemClick(task: Task) {
        util.openActivity(this,
            TaskActivity::class.java,
            EXTRA_MESSAGE_ID, task.id)
    }
}
