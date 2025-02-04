package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.WidgetRepository
import com.daniebeler.pfpixelix.utils.execute

class WidgetRepositoryImpl constructor(private val pixelfedApi: PixelfedApi): WidgetRepository{
    override suspend fun getNotifications(): Resource<List<com.daniebeler.pfpixelix.domain.model.Notification>> {
        try {
            val response = pixelfedApi.getNotifications().execute()
            val res = response.map { it.toModel() }
            return Resource.Success(res)
        } catch (e: Exception) {
            return Resource.Error("an unexpected error occured")
        }
    }

    override suspend fun getLatestImage(): Resource<Post> {
        try {
            val response = pixelfedApi.getHomeTimelineWithLimit(1).execute()
            val res = response.map { it.toModel() }
            if (res.isEmpty()) {
                return Resource.Error("an unexpected error occured")
            }
            return Resource.Success(res.first())
        } catch (e: Exception) {
            return Resource.Error("an unexpected error occured")
        }
    }
}