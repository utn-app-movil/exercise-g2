package cr.ac.utn.taskmanager

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
    private lateinit var spStatusFilter: Spinner

    private var searchText: String = ""
    private var selectedStatus: TaskStatus? = null

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
        svSearch = findViewById(R.id.svSearch_List)
        spStatusFilter = findViewById(R.id.spStatusFilter_List)

        taskAdapter = TaskListAdapter(TaskViewModel.filterTasks("", null), this)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.adapter = taskAdapter

        setupSearch()
        setupStatusFilter()
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

    private fun setupSearch() {
        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchText = query.orEmpty()
                applyFilters()
                svSearch.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchText = newText.orEmpty()
                applyFilters()
                return true
            }
        })
    }

    private fun setupStatusFilter() {
        val options = listOf(getString(R.string.TextFilterAll)) +
                TaskStatus.entries.map { it.name }
        val statusAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            options
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatusFilter.adapter = statusAdapter

        spStatusFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                selectedStatus = if (position == 0) null else TaskStatus.entries[position - 1]
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun applyFilters() {
        taskAdapter.updateTasks(TaskViewModel.filterTasks(searchText, selectedStatus))
    }

    private fun setupSwipeToDelete() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteWithUndo(position)
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                                     viewHolder: RecyclerView.ViewHolder,
                                     dX: Float, dY: Float,
                                     actionState: Int, isCurrentlyActive: Boolean) {
                val itemView = viewHolder.itemView
                val background = ColorDrawable(Color.RED)
                if (dX > 0) {
                    background.setBounds(itemView.left, itemView.top,
                        itemView.left + dX.toInt(), itemView.bottom)
                    background.draw(c)
                } else if (dX < 0) {
                    background.setBounds(itemView.right + dX.toInt(), itemView.top,
                        itemView.right, itemView.bottom)
                    background.draw(c)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive)
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recycler)
    }

    private fun deleteWithUndo(position: Int) {
        val task = taskAdapter.getTaskAt(position)
        val originalIndex = TaskViewModel.getTaskIndex(task.id)

        TaskViewModel.removeTask(task.id)
        taskAdapter.removeAt(position)

        Snackbar.make(recycler, R.string.TextTaskDeleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.TextUndo) {
                TaskViewModel.restoreTask(task, originalIndex)
                applyFilters()
            }
            .show()
    }
}