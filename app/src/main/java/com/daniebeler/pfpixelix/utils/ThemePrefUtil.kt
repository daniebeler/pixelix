package com.daniebeler.pfpixelix.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemePrefUtil {

    const val AMOLED = -2
    const val LIGHT = AppCompatDelegate.MODE_NIGHT_NO
    const val DARK = AppCompatDelegate.MODE_NIGHT_YES
    const val FOLLOW_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

    const val KEY_NIGHT_MODE = "pref_key_night_mode"

    const val ACTION_NIGHT_MODE_CHANGED = "action_night_mode_changed"

    fun getThemeModeValue(context: Context): Int {
        return context.pref.getInt(KEY_NIGHT_MODE, FOLLOW_SYSTEM)
    }

    fun apply(context: Context) {
        var mode = getThemeModeValue(context)
        if (mode == AMOLED) {
            mode = DARK
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
