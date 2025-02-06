package com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic

import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.pref

object SettingPrefUtil {
    const val ON = 1
    const val OFF = 0

    const val EXTRA_VALUE = "extra_value"

    const val ACTION_CLOSE_SETTING = "com.dede.easter_eggs.CloseSetting"

    fun getValue(context: KmpContext, key: String, default: Int): Int {
        return context.pref.getInt(key, default)
    }

    fun setValue(context: KmpContext, key: String, value: Int) {
        context.pref.putInt(key, value)
    }
}
