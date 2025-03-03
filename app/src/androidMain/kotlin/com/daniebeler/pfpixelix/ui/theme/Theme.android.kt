package com.daniebeler.pfpixelix.ui.theme

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.daniebeler.pfpixelix.domain.model.AppThemeMode.AMOLED
import com.daniebeler.pfpixelix.domain.model.AppThemeMode.DARK
import com.daniebeler.pfpixelix.domain.model.AppThemeMode.LIGHT
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.LocalKmpContext

@RequiresApi(Build.VERSION_CODES.S)
actual fun KmpContext.generateColorScheme(
    nightModeValue: Int,
    dynamicColor: Boolean,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): ColorScheme = if (dynamicColor) {
    when (nightModeValue) {
        AMOLED -> dynamicDarkColorScheme(this).toAmoled()
        DARK -> dynamicDarkColorScheme(this)
        else -> dynamicLightColorScheme(this)
    }
} else {
    when (nightModeValue) {
        AMOLED -> darkScheme.toAmoled()
        DARK -> darkScheme
        else -> lightScheme
    }
}

actual fun applySystemNightMode(mode: Int) {
    AppCompatDelegate.setDefaultNightMode(
        when (mode) {
            LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            AMOLED, DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    )
}

@Composable
actual fun ChangeSystemBarColors(mode: Int) {
    (LocalKmpContext.current as ComponentActivity).enableEdgeToEdge(
        when (mode) {
            LIGHT -> SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            )

            AMOLED, DARK -> SystemBarStyle.dark(
                Color.Transparent.toArgb()
            )

            else -> SystemBarStyle.dark(
                Color.Transparent.toArgb()
            )
        }

    )

}