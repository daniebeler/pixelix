package com.daniebeler.pfpixelix.utils

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

class ApplicationInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        val application = context.applicationContext as Application
        // apply compat style
        ThemePrefUtil.apply(application)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
       // ReflectionInitializer::class.java,
        GlobalContext.Initializer::class.java
    )
}