package com.daniebeler.pixelix.data.repository

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.dto.DtoInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.awaitResponse

class NetworkCall<M, D : DtoInterface<M>> {
    fun makeCall(call: Call<D>): Flow<Resource<M>> = flow {
        try {
            emit(Resource.Loading())
            val response = call.awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toModel()
                emit(Resource.Success(res!!))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }
}