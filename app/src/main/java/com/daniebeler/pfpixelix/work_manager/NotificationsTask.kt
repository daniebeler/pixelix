package com.daniebeler.pfpixelix.work_manager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.daniebeler.pfpixelix.domain.repository.WidgetRepository
import com.daniebeler.pfpixelix.widget.updateWidget
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationsTask @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: WidgetRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Log.i("Worker", "starting")
        // Do the work here--in this case, upload the images.
        val notifications = repository.getNotifications()
        val test = notifications.data!!.first().id
        Log.i("Worker", "baseUrl: $test")

        updateWidget(notifications.data, appContext)
        Log.i("Worker", "Updated")

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

}