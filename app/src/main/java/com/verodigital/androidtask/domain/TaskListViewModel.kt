package com.verodigital.androidtask.domain

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.verodigital.androidtask.data.datasource.Task
import com.verodigital.androidtask.data.datasource.local.Tasks
import com.verodigital.androidtask.data.datasource.local.mSharedPreferences
import com.verodigital.androidtask.data.repository.LocalTaskRepository
import com.verodigital.androidtask.data.repository.LoginRepository
import com.verodigital.androidtask.data.repository.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val mTasksRepository: TasksRepository,
    private val mLoginRepository: LoginRepository,
    private val mLocalTaskRepository: LocalTaskRepository
): ViewModel() {
    suspend fun getAllTasks(): Flow<List<Task>> {
        return mTasksRepository.getAllTasks(mLoginRepository.getAccessToken())
    }
    suspend fun insertTask(task: Tasks){
        mLocalTaskRepository.insertSingleTask(task)
    }
    suspend fun getAllLocalTasks() = mLocalTaskRepository.getAllTasks()
}