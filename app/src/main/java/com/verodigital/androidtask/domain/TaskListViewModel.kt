package com.verodigital.androidtask.domain

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.verodigital.androidtask.data.PeriodicTaskWorker
import com.verodigital.androidtask.data.datasource.Task
import com.verodigital.androidtask.data.datasource.local.mSharedPreferences
import com.verodigital.androidtask.data.repository.LocalTaskRepository
import com.verodigital.androidtask.data.repository.LoginRepository
import com.verodigital.androidtask.data.repository.TasksRepository
import com.verodigital.androidtask.util.StringtoDate
import com.verodigital.androidtask.util.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val mTasksRepository: TasksRepository,
    private val mLoginRepository: LoginRepository,
    private val mLocalTaskRepository: LocalTaskRepository,
    private val workManager: WorkManager
) : ViewModel() {
    private var accessToken: String? = null
    private var modifiedDateTime: String? = null
    private var currDateTime: String? = null
    private var getAllTaskResponse: Flow<List<Task>>? = null
    private var minutes: Long = 0
    suspend fun getAllTasks(context: Context): Flow<List<Task>> {
        accessToken = mSharedPreferences.getSharedPreference(context)?.getString("accessToken", "")
        modifiedDateTime = mSharedPreferences.getSharedPreference(context)?.getString("modifiedDateTime", "")
        currDateTime = getCurrentDate()
        if (modifiedDateTime.isNullOrEmpty()) {
            mSharedPreferences.getSharedPreferenceEditor(context)?.putString(
                "modifiedDateTime",
                currDateTime
            )?.commit()
        } else {

            val modifiedDateTime: Date = StringtoDate(modifiedDateTime)
            val currDateTime = StringtoDate(currDateTime)
            val diff: Long = abs(currDateTime.time - modifiedDateTime.time)
            val seconds = diff / 1000
            minutes = seconds / 60

        }

        if ((minutes!! >= 19 && !accessToken.isNullOrEmpty()) || accessToken.isNullOrEmpty()) {
            accessToken = mLoginRepository.getAccessToken()
            mSharedPreferences.getSharedPreferenceEditor(context)
                ?.putString("accessToken", accessToken)?.commit()
            currDateTime = getCurrentDate()
            mSharedPreferences.getSharedPreferenceEditor(context)?.putString(
                "modifiedDateTime",
                currDateTime
            )?.commit()
            getAllTaskResponse = mTasksRepository.getAllTasks(accessToken.toString())

        } else {
            getAllTaskResponse = mTasksRepository.getAllTasks(accessToken!!)
        }

        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(PeriodicTaskWorker::class.java, 16, TimeUnit.MINUTES)
                .build();
        workManager.enqueue(periodicWorkRequest)

        return getAllTaskResponse!!
    }

    suspend fun insertTask(task: Task) {
        mLocalTaskRepository.insertSingleTask(task)
    }

    suspend fun getAllLocalTasks() = mLocalTaskRepository.getAllTasks()

    fun filterTaskList(query: String?, taskList: List<Task>): ArrayList<Task> {
        var filteredTaskList: ArrayList<Task> = arrayListOf()
        for (i in taskList) {
            if (i.task?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.title?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.description?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.sort?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.wageType?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.BusinessUnitKey?.let {
                    i.BusinessUnitKey!!.lowercase(java.util.Locale.ROOT)
                        ?.contains(query?.lowercase()!!)!!
                } == true ||
                i.businessUnit?.lowercase(java.util.Locale.ROOT)
                    ?.contains(query?.lowercase()!!)!! ||
                i.parentTaskID?.lowercase(java.util.Locale.ROOT)
                    ?.contains(query?.lowercase()!!)!! ||
                i.preplanningBoardQuickSelect?.let {
                    i.preplanningBoardQuickSelect!!.lowercase(java.util.Locale.ROOT)
                        ?.contains(query?.lowercase()!!)!!
                } == true ||
                i.colorCode?.lowercase(java.util.Locale.ROOT)?.contains(query?.lowercase()!!)!! ||
                i.workingTime?.let {
                    i.workingTime!!.lowercase(java.util.Locale.ROOT)
                        ?.contains(query?.lowercase()!!)!!
                } == true ||
                i.isAvailableInTimeTrackingKioskMode.toString()?.lowercase(java.util.Locale.ROOT)
                    .contains(query?.lowercase()!!)
            ) {
                filteredTaskList.add(i)
            }
        }
        return filteredTaskList;

    }

}