package com.daniebeler.pfpixelix.ui.theme

import androidx.compose.material3.ColorScheme
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.ThemePrefUtil

actual fun KmpContext.generateColorScheme(
    nightModeValue: Int,
    dynamicColor: Boolean,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): ColorScheme {
    //TODO dynamicColor
    return when (nightModeValue) {
        ThemePrefUtil.AMOLED -> darkScheme.toAmoled()
        ThemePrefUtil.DARK -> darkScheme
        else -> lightScheme
    }
}