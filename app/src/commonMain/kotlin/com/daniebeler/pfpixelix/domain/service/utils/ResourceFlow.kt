package com.daniebeler.pfpixelix.domain.service.utils

import com.daniebeler.pfpixelix.domain.service.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal inline fun <reified T> loadResource(
    crossinline call: suspend () -> T
): Flow<Resource<T>> = flow {
    emit(Resource.Loading())
    try {
        val data = call()
        emit(Resource.Success(data))
    } catch (e: Exception) {
        emit(Resource.Error(e.message ?: "Unknown Error"))
    }
}

internal inline fun <reified T> loadListResources(
    crossinline call: suspend () -> List<T>
): Flow<Resource<List<T>>> = flow {
    emit(Resource.Loading())
    try {
        val data = call()
        emit(Resource.Success(data))
    } catch (e: Exception) {
        emit(Resource.Error(e.message ?: "Unknown Error"))
    }
}