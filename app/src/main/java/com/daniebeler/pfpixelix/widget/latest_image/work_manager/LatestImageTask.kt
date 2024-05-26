package com.daniebeler.pfpixelix.widget.notifications.work_manager

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.FileProvider.getUriForFile
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.widget.WidgetRepositoryProvider
import com.daniebeler.pfpixelix.widget.latest_image.updateLatestImageWidget
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.net.UnknownHostException

@HiltWorker
class LatestImageTask @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataStore: DataStore<Preferences>
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            Log.i("Worker", "starting")
            val repository = WidgetRepositoryProvider(dataStore).invoke()
            val res = repository.getLatestImage()
            if (res is Resource.Success && res.data != null) {

                val imageUri = getImageUri(res.data.mediaAttachments.first().previewUrl)

                updateLatestImageWidget(imageUri, context)
                Log.i("Worker", "latestImage updated")
            }
        } catch (e: Exception) {
            if (e is UnknownHostException) {
                Log.e("Worker", "unknown host, retry")
                return Result.retry()
            }
            return Result.failure()
        }
        return Result.success()
    }

    private suspend fun getImageUri(url: String): String {
        val request = ImageRequest.Builder(context).data(url).build()

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
                context, "com.example.android.appwidget.fileprovider", imageFile
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