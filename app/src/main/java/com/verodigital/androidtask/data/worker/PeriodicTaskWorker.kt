package com.verodigital.androidtask.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.verodigital.androidtask.BaseApplication
import com.verodigital.androidtask.data.repository.TasksRepository
import com.verodigital.androidtask.domain.TaskListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class PeriodicTaskWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) : Worker(context,
    workerParams
) {
    override fun doWork(): Result {

        val localTaskRepository = (context as BaseApplication).localTaskRepository
        CoroutineScope(Dispatchers.IO).launch{
            localTaskRepository.updateTasks(context)
        }
        Log.d(TAG, "doWork: Work is Completed")
        return Result.success()
    }

    companion object {
        private const val TAG = "PeriodicTaskWorker"
    }
}