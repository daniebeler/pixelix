package com.daniebeler.pfpixelix.utils

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import de.jensklingenberg.ktorfit.Call
import de.jensklingenberg.ktorfit.Callback
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkCall<M, D : DtoInterface<M>> {
    fun makeCall(call: Call<D>): Flow<Resource<M>> = flow {
        try {
            emit(Resource.Loading())
            try {
                val res = call.execute().toModel()
                emit(Resource.Success(res!!))
            } catch (e: Exception) {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    fun makeCallList(call: Call<List<D>>): Flow<Resource<List<M>>> = flow {
        try {
            emit(Resource.Loading())
            try {
                val res = call.execute().map { it.toModel() }
                emit(Resource.Success(res))
            } catch (e: Exception) {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }
}

internal suspend fun <T> Call<T>.execute() = suspendCoroutine { cont ->
    onExecute(object : Callback<T> {
        override fun onResponse(call: T, response: HttpResponse) {
            cont.resume(call)
        }

        override fun onError(exception: Throwable) {
            cont.resumeWithException(exception)
        }
    })
}

internal suspend fun <T> Call<T>.executeWithResponse() = suspendCoroutine { cont ->
    onExecute(object : Callback<T> {
        override fun onResponse(call: T, response: HttpResponse) {
            cont.resume(response to call)
        }

        override fun onError(exception: Throwable) {
            cont.resumeWithException(exception)
        }
    })
}