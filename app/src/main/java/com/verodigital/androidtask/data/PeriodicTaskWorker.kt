package com.verodigital.androidtask.data

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters


class PeriodicTaskWorker(context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {
    override fun doWork(): Result {
        Log.e(TAG, "doWork: Work is Completed")
        return Result.success()
    }

    companion object {
        private const val TAG = "PeriodicTaskWorker"
    }
}