package com.daniebeler.pfpixelix.domain.service.widget

import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.domain.service.utils.loadListResources
import com.daniebeler.pfpixelix.domain.service.utils.loadResource
import me.tatarka.inject.annotations.Inject

@Inject
class WidgetService(
    private val api: PixelfedApi
) {
    fun getNotifications(maxNotificationId: String = "") = loadListResources {
        if (maxNotificationId.isNotEmpty()) {
            api.getNotifications(maxNotificationId)
        } else {
            api.getNotifications()
        }
    }

    fun getLatestImage() = loadResource {
        api.getHomeTimelineWithLimit(1).first()
    }
}