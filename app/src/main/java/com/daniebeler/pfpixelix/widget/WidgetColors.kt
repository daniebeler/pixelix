package com.daniebeler.pfpixelix.widget


import androidx.compose.material3.darkColorScheme
import androidx.glance.material3.ColorProviders
import com.daniebeler.pfpixelix.ui.theme.Pink80
import com.daniebeler.pfpixelix.ui.theme.Purple80
import com.daniebeler.pfpixelix.ui.theme.PurpleGrey80

object WidgetColors {
    val colors = ColorProviders(
        light = DarkColorScheme,
        dark = DarkColorScheme
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)