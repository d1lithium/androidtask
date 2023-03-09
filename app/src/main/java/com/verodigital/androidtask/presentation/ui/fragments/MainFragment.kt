package com.verodigital.androidtask.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.verodigital.androidtask.R
import com.verodigital.androidtask.data.datasource.local.Tasks
import com.verodigital.androidtask.domain.TaskListViewModel
import com.verodigital.androidtask.presentation.ui.adapters.LocalTaskAdapter
import com.verodigital.androidtask.presentation.ui.adapters.TaskAdapter
import com.verodigital.androidtask.util.getProgressDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private val taskListViewModel: TaskListViewModel by viewModels()
    private val taskAdapter: TaskAdapter = TaskAdapter(arrayListOf())
    private val localTaskAdapter: LocalTaskAdapter = LocalTaskAdapter(arrayListOf())
    private var taskList: List<Tasks> = arrayListOf()
    private var v: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        lifecycleScope.launch {
            //populateList()
            populateListFromLocalDB()

        }
        setTaskAdapter("local",v!!)
        taskSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTaskList(newText)
                return true
            }

        })

    }

    private fun filterTaskList(query: String?) {
        var filteredTaskList: ArrayList<Tasks> = arrayListOf()
        for (i in taskList) {
            if (i.task?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.title?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.description?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.sort?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.wageType?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.BusinessUnitKey?.let { i.BusinessUnitKey!!.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!!} == true ||
                i.businessUnit?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.parentTaskID?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.preplanningBoardQuickSelect?.let { i.preplanningBoardQuickSelect!!.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!!} == true ||
                i.colorCode?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.workingTime?.let { i.workingTime!!.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!!} == true ||
                i.isAvailableInTimeTrackingKioskMode.toString()?.lowercase(java.util.Locale.ROOT)
                    .contains(query?.lowercase()!!)
            ) {
                filteredTaskList.add(i)
            }
        }
        localTaskAdapter.updateTasks(filteredTaskList)

    }

    private suspend fun populateList(){


                taskListViewModel.getAllTasks().collectLatest {

                    //  println("localtasks--->"+localTasks.toList())
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

    private suspend fun populateListFromLocalDB() {
    /*    for (i in 0..21) {
            taskListViewModel.insertTask(
                Tasks(
                    "task$i",
                    "title$i",
                    "description$i",
                    "sort$i",
                    "wageType$i",
                    "BusinessUnitKey$i",
                    "businessUnit$i",
                    "parentTaskID$i",
                    "preplanningBoardQuickSelect$i",
                    "#FF03DAC5",
                    "workingTime$i",
                    null
                )
            )
        }
        */
        taskListViewModel.getAllLocalTasks().collectLatest {
            taskList = it
            localTaskAdapter.updateTasks(it)
        }
        v?.let { setTaskAdapter("local", it) }
    }

    private fun setTaskAdapter(flag: String, view: View) {
        if (flag.equals("local")) {
            recyclerViewTask.apply {
                layoutManager = LinearLayoutManager(view.context)
                adapter = localTaskAdapter
            }
        } else if (flag.equals("remote")) {

            recyclerViewTask.apply {
                layoutManager = LinearLayoutManager(view.context)
                adapter = taskAdapter

            }
        }
    }


}

