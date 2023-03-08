package com.verodigital.androidtask.data.repository

import android.widget.Toast
import com.verodigital.androidtask.data.datasource.Task
import com.verodigital.androidtask.data.datasource.local.Tasks
import com.verodigital.androidtask.data.datasource.local.TasksDao
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class LocalTaskRepository @Inject constructor(
    private val tasksDao: TasksDao
) {
   suspend fun insertSingleTask(task: Tasks){
       try {
           tasksDao.insert(task)
           println("Successfully inserted single task")
       }
       catch (e: Exception ){
           println(e.localizedMessage)
       }


   }
}