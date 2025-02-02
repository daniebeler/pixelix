package com.daniebeler.pfpixelix.utils

import android.annotation.SuppressLint
import android.content.Context


val globalContext: Context
    get() = GlobalContext.globalContext

@SuppressLint("StaticFieldLeak")
object GlobalContext {

    internal lateinit var globalContext: Context
        private set

    class Initializer : androidx.startup.Initializer<Unit> {
        override fun create(context: Context) {
            globalContext = context
        }

        override fun dependencies(): List<Class<out androidx.startup.Initializer<*>>> = emptyList()
    }
}