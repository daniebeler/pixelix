package com.daniebeler.pfpixelix.domain.service.utils

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


internal inline fun <reified T> loadResource(
    crossinline call: suspend () -> DtoInterface<T>
): Flow<Resource<T>> = flow {
    emit(Resource.Loading())
    try {
        val data = call().toModel()
        emit(Resource.Success(data))
    } catch (e: Exception) {
        emit(Resource.Error(e.message ?: "Unknown Error"))
    }
}


internal inline fun <reified T> loadType(
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

internal inline fun loadUnit(
    crossinline call: suspend () -> Unit
): Flow<Resource<Unit>> = flow {
    emit(Resource.Loading())
    try {
        call()
        emit(Resource.Success(Unit))
    } catch (e: Exception) {
        emit(Resource.Error(e.message ?: "Unknown Error"))
    }
}

internal inline fun <reified T> loadListResources(
    crossinline call: suspend () -> List<DtoInterface<T>>
): Flow<Resource<List<T>>> = flow {
    emit(Resource.Loading())
    try {
        val data = call().map { it.toModel() }
        emit(Resource.Success(data))
    } catch (e: Exception) {
        emit(Resource.Error(e.message ?: "Unknown Error"))
    }
}