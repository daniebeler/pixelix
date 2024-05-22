package com.daniebeler.pfpixelix.work_manager

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.FileProvider.getUriForFile
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.repository.WidgetRepository
import com.daniebeler.pfpixelix.widget.models.NotificationStoreItem
import com.daniebeler.pfpixelix.widget.updateWidget
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationsTask @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: WidgetRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        Log.i("Worker", "starting")
        // Do the work here--in this case, upload the images.
        val res = repository.getNotifications()
        if (res is Resource.Success && res.data != null) {
            val notifications = res.data.take(10)
            val notificationStoreItems = notifications.map{notification ->
                val accountAvatarUri = getImageUri(notification.account.avatar)
                NotificationStoreItem(notification.id, notification.account.avatar, accountAvatarUri, notification.type)
            }
            updateWidget(notificationStoreItems, context)
            Log.i("Worker", "Updated")
        }

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    private suspend fun getImageUri(url: String): String {
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()

        // Request the image to be loaded and throw error if it failed
        with(context.imageLoader) {
            val result = execute(request)
            if (result is ErrorResult) {
                throw result.throwable
            }
        }

        // Get the path of the loaded image from DiskCache.
        val path = context.imageLoader.diskCache?.get(url)?.use { snapshot ->
            val imageFile = snapshot.data.toFile()

            // Use the FileProvider to create a content URI
            val contentUri = getUriForFile(
                context,
                "com.example.android.appwidget.fileprovider",
                imageFile
            )

            // Find the current launcher everytime to ensure it has read permissions
            val resolveInfo = context.packageManager.resolveActivity(
                Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) },
                PackageManager.MATCH_DEFAULT_ONLY
            )
            val launcherName = resolveInfo?.activityInfo?.packageName
            if (launcherName != null) {
                context.grantUriPermission(
                    launcherName,
                    contentUri,
                    FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                )
            }

            // return the path
            contentUri.toString()
        }
        return requireNotNull(path) {
            "Couldn't find cached file"
        }
    }

}