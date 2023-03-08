package com.verodigital.androidtask.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.verodigital.androidtask.R
import com.verodigital.androidtask.data.datasource.local.Tasks
import com.verodigital.androidtask.domain.TaskListViewModel
import com.verodigital.androidtask.presentation.ui.adapters.TaskAdapter
import com.verodigital.androidtask.util.getProgressDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private val taskListViewModel: TaskListViewModel by viewModels()
    private val taskAdapter: TaskAdapter = TaskAdapter(arrayListOf())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {

            getProgressDrawable(view.context)
            populateList(view)

        }
        recyclerViewTask.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter

        }


    }

    suspend fun populateList(view: View) {
        taskListViewModel.getAllTasks().collectLatest {
            taskAdapter.updateTasks(it)
            for (i in it.indices) {
                taskListViewModel.insertTask(
                    Tasks(
                        it[i].task!!,
                        it[i].title,
                        it[i].description,
                        it[i].sort,
                        it[i].wageType,
                        it[i].BusinessUnitKey,
                        it[i].businessUnit,
                        it[i].parentTaskID,
                        it[i].preplanningBoardQuickSelect,
                        it[i].colorCode,
                        it[i].workingTime,
                        it[i].isAvailableInTimeTrackingKioskMode
                    )
                )
            }


        }


    }

}

