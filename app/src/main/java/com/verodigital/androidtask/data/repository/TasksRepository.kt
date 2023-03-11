package com.verodigital.androidtask.data.repository

import androidx.work.impl.Schedulers
import com.verodigital.androidtask.data.datasource.Task
import com.verodigital.androidtask.data.datasource.remote.api.LoginService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.util.*
import javax.inject.Inject

class TasksRepository @Inject constructor(
    private val loginService: LoginService,
    private val loginRepository: LoginRepository
) {
  suspend fun getAllTasks(authorizationHeader: String): Flow<List<Task>> {
      val getAllTasksResponse = loginService.getAllTasks(authorizationHeader)
      return {getAllTasksResponse}.asFlow()
    }





}