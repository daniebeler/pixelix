package com.daniebeler.pixelix.di

import okhttp3.Interceptor

interface HostSelectionInterceptorInterface: Interceptor {

    fun setHost(host: String?)
    fun setToken(token: String?)
}