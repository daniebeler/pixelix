package com.daniebeler.pfpixelix.ui.theme

import androidx.compose.material3.ColorScheme
import com.daniebeler.pfpixelix.domain.model.AppThemeMode
import com.daniebeler.pfpixelix.utils.KmpContext

actual fun applySystemNightMode(mode: Int) {}

actual fun KmpContext.generateColorScheme(
    nightModeValue: Int,
    dynamicColor: Boolean,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): ColorScheme {
    //TODO dynamicColor
    return when (nightModeValue) {
        AppThemeMode.AMOLED -> darkScheme.toAmoled()
        AppThemeMode.DARK -> darkScheme
        else -> lightScheme
    }
}