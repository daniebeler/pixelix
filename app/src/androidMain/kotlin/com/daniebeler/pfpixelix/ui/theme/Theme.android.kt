package com.daniebeler.pfpixelix.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import com.daniebeler.pfpixelix.domain.model.AppThemeMode
import com.daniebeler.pfpixelix.utils.KmpContext

@RequiresApi(Build.VERSION_CODES.S)
actual fun KmpContext.generateColorScheme(
    nightModeValue: Int,
    dynamicColor: Boolean,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): ColorScheme = if (dynamicColor) {
    when (nightModeValue) {
        AppThemeMode.AMOLED -> dynamicDarkColorScheme(this).toAmoled()
        AppThemeMode.DARK -> dynamicDarkColorScheme(this)
        else -> dynamicLightColorScheme(this)
    }
} else {
    when (nightModeValue) {
        AppThemeMode.AMOLED -> darkScheme.toAmoled()
        AppThemeMode.DARK -> darkScheme
        else -> lightScheme
    }
}