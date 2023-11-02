package com.global.sarthi.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


object WorkerCaller {
    fun callLocationWorker(context: Context) {
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()
        val workRequest = PeriodicWorkRequest
            .Builder(
                LocationWorker::class.java,
                30,
                TimeUnit.MINUTES
            ).setConstraints(constraint)
            .addTag("Location_Worker")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "LocationWorker",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workRequest
        )
    }
}