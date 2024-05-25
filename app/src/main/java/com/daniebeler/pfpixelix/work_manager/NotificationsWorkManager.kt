package com.daniebeler.pfpixelix.work_manager

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit


class NotificationsWorkManager(private val context: Context) {
    fun executePeriodic() = enqueuePeriodicWorker()
    fun executeOnce() = startWorkerOnce()

    private fun startWorkerOnce() {
        val uploadWorkerRequest: WorkRequest = OneTimeWorkRequestBuilder<NotificationsTask>().build()
        WorkManager.getInstance(context).enqueue(uploadWorkerRequest)
    }

    private fun enqueuePeriodicWorker() {
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_read_worker_tag",
            // KEEP documentation:
            // If there is existing pending (uncompleted) work with the same unique name, do nothing.
            // Otherwise, insert the newly-specified work.
            ExistingPeriodicWorkPolicy.KEEP,
            buildRequest()
        )
    }

    private fun buildRequest(): PeriodicWorkRequest {
        // 1 day
        return PeriodicWorkRequestBuilder<NotificationsTask>(15, TimeUnit.MINUTES)
            .addTag("daily_read_worker_tag")
            .setConstraints(
                Constraints.Builder()
                    // Network required
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
    }

    fun cancelWork() {
        WorkManager
            .getInstance(context)
            .cancelUniqueWork("daily_read_worker_tag")
    }
}