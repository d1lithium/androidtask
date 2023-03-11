package com.verodigital.androidtask

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.verodigital.androidtask.data.repository.LocalTaskRepository
import com.verodigital.androidtask.data.worker.PeriodicTaskWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication() : Application() {
    @Inject
   lateinit var localTaskRepository: LocalTaskRepository
    override fun onCreate() {
        super.onCreate()
        setTaskWorker()
    }

    private fun setTaskWorker() {

        val netConstraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicTaskWorkRequest = PeriodicWorkRequest.Builder(PeriodicTaskWorker::class.java, 60, TimeUnit.MINUTES)
            .setConstraints(netConstraint).build()
        WorkManager.getInstance(this).enqueue(periodicTaskWorkRequest)

    }

}