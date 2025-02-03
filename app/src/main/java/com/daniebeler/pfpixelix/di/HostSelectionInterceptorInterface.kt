package com.daniebeler.pfpixelix.di

import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder

interface HostSelectionInterceptorInterface {

    fun setHost(host: String?)
    fun setToken(token: String?)

    suspend fun Sender.intercept(request: HttpRequestBuilder): HttpClientCall
}