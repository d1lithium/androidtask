package com.verodigital.androidtask.data.repository

import android.widget.Toast
import com.verodigital.androidtask.data.datasource.Task
import com.verodigital.androidtask.data.datasource.local.TasksDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class LocalTaskRepository @Inject constructor(
    private val tasksDao: TasksDao
) {
   suspend fun insertSingleTask(task: Task){
       try {
           tasksDao.insert(task)
           println("Successfully inserted single task")
       }
       catch (e: Exception ){
           println(e.localizedMessage)
       }


   }
    suspend fun getAllTasks() = tasksDao.getAllTasks()

}