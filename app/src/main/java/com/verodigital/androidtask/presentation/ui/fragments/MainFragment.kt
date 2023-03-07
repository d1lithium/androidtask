package com.verodigital.androidtask.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.verodigital.androidtask.R
import com.verodigital.androidtask.domain.LoginViewModel
import com.verodigital.androidtask.domain.TaskListViewModel
import com.verodigital.androidtask.presentation.ui.adapters.TaskAdapter
import com.verodigital.androidtask.util.getProgressDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment: Fragment(R.layout.fragment_main) {
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
    suspend fun populateList(view: View){
        taskListViewModel.getAllTasks().collectLatest {
            getProgressDrawable(view.context).stop()
            println("--->"+it)
            taskAdapter.updateTasks(it)


        }


    }

    }

