package com.daniebeler.pfpixelix.ui.composables.settings.preferences

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrightnessAuto
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.theme.themeMode
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.AMOLED
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.DARK
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.FOLLOW_SYSTEM
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.KEY_NIGHT_MODE
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.LIGHT
import com.daniebeler.pfpixelix.utils.rememberPrefIntState


@Composable
fun ThemePref() {
    var themeModeValue by rememberPrefIntState(KEY_NIGHT_MODE, FOLLOW_SYSTEM)
    val onOptionClick = click@{ mode: Int ->
        if (themeModeValue == mode) {
            return@click
        }
        themeModeValue = mode
        themeMode = themeModeValue
        var appCompatMode = mode
        if (appCompatMode == AMOLED) {
            appCompatMode = DARK
        }
        AppCompatDelegate.setDefaultNightMode(appCompatMode)
    }

    ExpandOptionsPref(
        leadingIcon = Icons.Rounded.Brush,
        title = stringResource(R.string.app_theme),
    ) {
        ValueOption(
            shape = OptionShapes.indexOfShape(0, 3),
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.BrightnessAuto,
                contentDescription = stringResource(R.string.theme_system)
            ),
            title = stringResource(R.string.theme_system),
            trailingContent = radioButtonBlock(themeModeValue == FOLLOW_SYSTEM),
            value = FOLLOW_SYSTEM,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            shape = OptionShapes.indexOfShape(1, 3),
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.LightMode,
                contentDescription = stringResource(R.string.theme_light)
            ),
            title = stringResource(R.string.theme_light),
            trailingContent = radioButtonBlock(themeModeValue == LIGHT),
            value = LIGHT,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            shape = OptionShapes.indexOfShape(2, 3),
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.DarkMode,
                contentDescription = stringResource(R.string.theme_dark)
            ),
            title = stringResource(R.string.theme_dark),
            trailingContent = radioButtonBlock(themeModeValue == DARK),
            value = DARK,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            shape = OptionShapes.indexOfShape(2, 3),
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.Contrast,
                contentDescription = stringResource(R.string.amoled)
            ),
            title = stringResource(R.string.amoled),
            trailingContent = radioButtonBlock(themeModeValue == AMOLED),
            value = AMOLED,
            onOptionClick = onOptionClick,
        )

    }
}