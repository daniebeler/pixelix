package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.domain.repository.WidgetRepository
import retrofit2.awaitResponse
import javax.inject.Inject

class WidgetRepositoryImpl @Inject constructor(private val pixelfedApi: PixelfedApi): WidgetRepository{
    override suspend fun getNotifications(): Resource<List<com.daniebeler.pfpixelix.domain.model.Notification>> {
        val response = pixelfedApi.getNotifications().awaitResponse()
        if (response.isSuccessful) {
            val res = response.body()?.map { it.toModel() } ?: emptyList()
            return Resource.Success(res)
        } else {
            return Resource.Error("an unexpected error occured")
        }
    }
}