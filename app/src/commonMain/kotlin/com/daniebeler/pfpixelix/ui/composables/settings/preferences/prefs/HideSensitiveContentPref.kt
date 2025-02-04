package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import com.daniebeler.pfpixelix.utils.KmpContext

expect object HideSensitiveContentPrefUtil {
    fun isEnable(context: KmpContext): Boolean
}