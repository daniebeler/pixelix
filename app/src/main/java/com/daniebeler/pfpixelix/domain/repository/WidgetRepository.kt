package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Notification

interface WidgetRepository {
    suspend fun getNotifications(): Resource<List<Notification>>
}