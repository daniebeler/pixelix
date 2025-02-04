package com.daniebeler.pfpixelix.utils

import android.content.Context
import android.content.SharedPreferences


val Context.pref: SharedPreferences
    get() {
        return applicationContext.getSharedPreferences(
            applicationContext.packageName + "_preferences",
            Context.MODE_PRIVATE
        )
    }
