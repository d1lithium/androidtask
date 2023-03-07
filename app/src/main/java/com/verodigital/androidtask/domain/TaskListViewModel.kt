package com.verodigital.androidtask.domain

import androidx.lifecycle.ViewModel
import com.verodigital.androidtask.data.datasource.getAllTasksResponse
import com.verodigital.androidtask.data.datasource.local.Task
import com.verodigital.androidtask.data.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskListViewModel @Inject constructor(
    private val mTasksRepository: TasksRepository
): ViewModel() {

    suspend fun getAllTasks(): Flow<getAllTasksResponse> {
        return mTasksRepository.getAllTasks()
    }
}