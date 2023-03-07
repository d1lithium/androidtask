package com.verodigital.androidtask.data.repository

import com.verodigital.androidtask.data.datasource.getAllTasksResponse
import com.verodigital.androidtask.data.datasource.local.Task
import com.verodigital.androidtask.data.datasource.remote.api.LoginService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

class TasksRepository @Inject constructor(
    private val loginService: LoginService
) {
  suspend fun getAllTasks(authorizationHeader: String): Flow<List<Task>> {
      val getAllTasksResponse = loginService.getAllTasks(authorizationHeader)
      return {getAllTasksResponse}.asFlow()
    }
}