package com.verodigital.androidtask.data.repository

import android.content.Context
import android.widget.Toast
import com.verodigital.androidtask.data.datasource.Task
import com.verodigital.androidtask.data.datasource.local.TasksDao
import com.verodigital.androidtask.data.datasource.local.mSharedPreferences
import com.verodigital.androidtask.util.StringtoDate
import com.verodigital.androidtask.util.getCurrentDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.math.abs

class LocalTaskRepository @Inject constructor(
    private val tasksDao: TasksDao,
    private val loginRepository: LoginRepository,
    private val tasksRepository: TasksRepository,

    ) {
    private var accessToken: String? = null
    private var currDateTime: String? = null
    private var getAllTaskResponse: Flow<List<Task>>? = null
    suspend fun insertSingleTask(task: Task) {
        try {
            tasksDao.insert(task)
            println("Successfully inserted single task")
        } catch (e: Exception) {
            println(e.localizedMessage)
        }


    }
    suspend fun insertAllTasks(tasklist: List<Task>) {
        try {
            tasksDao.insertAll(tasklist)
            println("Successfully inserted All tasks")
        } catch (e: Exception) {
            println(e.localizedMessage)
        }


    }

    suspend fun getAllTasks() = tasksDao.getAllTasks()

    suspend fun updateTasks(context: Context) {

        accessToken = loginRepository.getAccessToken()
        mSharedPreferences.getSharedPreferenceEditor(context)
            ?.putString("accessToken", accessToken)?.commit()
        currDateTime = getCurrentDate()
        mSharedPreferences.getSharedPreferenceEditor(context)?.putString(
            "modifiedDateTime",
            currDateTime
        )?.commit()
        getAllTaskResponse = tasksRepository.getAllTasks(accessToken.toString())
        getAllTaskResponse!!.collectLatest {
            tasksDao.insertAll(it)

        }


    }

}