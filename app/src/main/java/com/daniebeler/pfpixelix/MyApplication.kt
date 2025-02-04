package com.daniebeler.pfpixelix

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.daniebeler.pfpixelix.di.AppComponent
import com.daniebeler.pfpixelix.di.WorkerComponent
import com.daniebeler.pfpixelix.di.create
import com.daniebeler.pfpixelix.widget.notifications.work_manager.LatestImageTask
import com.daniebeler.pfpixelix.widget.notifications.work_manager.NotificationsTask


class MyApplication : Application(), Configuration.Provider, ImageLoaderFactory {

    private val workerFactory: WorkerFactory by lazy { MyWorkerFactory(appComponent) }
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache(
                MemoryCache.Builder(this)
                    .maxSizePercent(0.2)
                    .build()
            )
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache(DiskCache.Builder()
                .maxSizeBytes(50L * 1024L * 1024L)
                .directory(cacheDir)
                .build())
            .build()
    }

    override fun onCreate() {
        appComponent = AppComponent::class.create(this)
        super.onCreate()
    }

    companion object {
        lateinit var appComponent: AppComponent
            private set
    }
}

private class MyWorkerFactory(
    private val appComponent: AppComponent
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerComponent = WorkerComponent::class.create(
            appComponent,
            appContext,
            workerParameters
        )
        return when(workerClassName) {
            NotificationsTask::class.java.name -> workerComponent.notificationsTask
            LatestImageTask::class.java.name -> workerComponent.latestImageTask
            else -> null
        }
    }
}