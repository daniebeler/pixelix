package com.daniebeler.pfpixelix.di

import android.content.Context
import androidx.work.WorkerParameters
import com.daniebeler.pfpixelix.widget.notifications.work_manager.LatestImageTask
import com.daniebeler.pfpixelix.widget.notifications.work_manager.NotificationsTask
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class WorkerComponent(
    @Component val appComponent: AppComponent,
    @get:Provides val context: Context,
    @get:Provides val workerParameters: WorkerParameters
) {
    abstract val notificationsTask: NotificationsTask
    abstract val latestImageTask: LatestImageTask
}