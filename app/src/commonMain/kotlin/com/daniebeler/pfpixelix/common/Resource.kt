package com.daniebeler.pfpixelix.common

sealed class Resource<T>(open val data: T? = null, open val message: String? = null) {
    class Success<T>(override val data: T) : Resource<T>(data)
    class Error<T>(override val message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}