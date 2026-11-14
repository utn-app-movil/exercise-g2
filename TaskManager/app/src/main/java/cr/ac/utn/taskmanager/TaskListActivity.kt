package cr.ac.utn.taskmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.text.Editable
import android.text.TextWatcher
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val recycler = findViewById<RecyclerView>(R.id.rvTasks_List)

        val taskAdapter = TaskListAdapter(
            TaskViewModel.getTasks(),
            this
        )

        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = taskAdapter

        val spStatus = findViewById<Spinner>(R.id.spStatus_List)

        val statusOptions = arrayOf(
            getString(R.string.FilterAll),
            getString(R.string.statsPending),
            getString(R.string.statsInProgress),
            getString(R.string.statsCompleted),
            getString(R.string.statsCancelled)
        )

        val statusAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statusOptions
        )

        statusAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spStatus.adapter = statusAdapter

        val txtSearch = findViewById<EditText>(R.id.txtSearch_List)


        fun filterTasks(searchText: String, selectedStatus: Int) {

            val filteredTasks = TaskViewModel.getTasks().filter { task ->

                val matchesText = task.title.contains(
                    searchText,
                    ignoreCase = true
                )

                val matchesStatus = when (selectedStatus) {
                    1 -> task.status == TaskStatus.PENDING
                    2 -> task.status == TaskStatus.IN_PROGRESS
                    3 -> task.status == TaskStatus.COMPLETED
                    4 -> task.status == TaskStatus.CANCELLED
                    else -> true
                }

                matchesText && matchesStatus
            }

            taskAdapter.updateTasks(filteredTasks)
        }


        txtSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                filterTasks(
                    s.toString(),
                    spStatus.selectedItemPosition
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                filterTasks(
                    txtSearch.text.toString(),
                    position
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val itemTouchHelper = ItemTouchHelper(
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
                    val position = viewHolder.bindingAdapterPosition

                    val filteredTasks = TaskViewModel.getTasks().filter { task ->
                        val matchesText = task.title.contains(
                            txtSearch.text.toString(),
                            ignoreCase = true
                        )

                        val matchesStatus = when (spStatus.selectedItemPosition) {
                            1 -> task.status == TaskStatus.PENDING
                            2 -> task.status == TaskStatus.IN_PROGRESS
                            3 -> task.status == TaskStatus.COMPLETED
                            4 -> task.status == TaskStatus.CANCELLED
                            else -> true
                        }

                        matchesText && matchesStatus
                    }

                    if (position < 0 || position >= filteredTasks.size) {
                        return
                    }

                    val task = filteredTasks[position]

                    TaskViewModel.removeTask(task.id)

                    filterTasks(
                        txtSearch.text.toString(),
                        spStatus.selectedItemPosition
                    )

                    Snackbar.make(
                        findViewById(R.id.main),
                        getString(R.string.TaskDeleted),
                        Snackbar.LENGTH_LONG
                    ).setAction(getString(R.string.TaskUndo)) {

                        TaskViewModel.saveTask(task)

                        filterTasks(
                            txtSearch.text.toString(),
                            spStatus.selectedItemPosition
                        )

                    }.show()
                }
            }
        )

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
