package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrightnessAuto
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.ExpandOptionsPref
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.OptionShapes
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.ValueOption
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.imageVectorIconBlock
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.radioButtonBlock
import com.daniebeler.pfpixelix.ui.theme.LocalTheme
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.AMOLED
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.DARK
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.FOLLOW_SYSTEM
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.KEY_NIGHT_MODE
import com.daniebeler.pfpixelix.utils.ThemePrefUtil.LIGHT
import com.daniebeler.pfpixelix.utils.rememberPrefIntState

@Preview
@Composable
fun ThemePref() {
    AppCompatDelegate.MODE_NIGHT_NO
    var themeModeValue by rememberPrefIntState(KEY_NIGHT_MODE, FOLLOW_SYSTEM)
    var appTheme by LocalTheme.current

    val onOptionClick = click@{ mode: Int ->
        if (themeModeValue == mode) {
            return@click
        }
        themeModeValue = mode
        appTheme = themeModeValue
        var appCompatMode = mode
        if (appCompatMode == AMOLED) {
            appCompatMode = DARK
        }

        AppCompatDelegate.setDefaultNightMode(
            when (appCompatMode) {
                LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                DARK -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }

    ExpandOptionsPref(
        leadingIcon = Res.drawable.color_palette_outline,
        title = stringResource(Res.string.app_theme),
    ) {
        ValueOption(
            shape = OptionShapes.indexOfShape(0, 3),
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.BrightnessAuto,
                contentDescription = stringResource(Res.string.theme_system)
            ),
            title = stringResource(Res.string.theme_system),
            trailingContent = radioButtonBlock(themeModeValue == FOLLOW_SYSTEM),
            value = FOLLOW_SYSTEM,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            shape = OptionShapes.indexOfShape(1, 3),
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.LightMode,
                contentDescription = stringResource(Res.string.theme_light)
            ),
            title = stringResource(Res.string.theme_light),
            trailingContent = radioButtonBlock(themeModeValue == LIGHT),
            value = LIGHT,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            shape = OptionShapes.indexOfShape(2, 3),
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.DarkMode,
                contentDescription = stringResource(Res.string.theme_dark)
            ),
            title = stringResource(Res.string.theme_dark),
            trailingContent = radioButtonBlock(themeModeValue == DARK),
            value = DARK,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            shape = OptionShapes.indexOfShape(2, 3),
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.Contrast,
                contentDescription = stringResource(Res.string.amoled)
            ),
            title = stringResource(Res.string.amoled),
            trailingContent = radioButtonBlock(themeModeValue == AMOLED),
            value = AMOLED,
            onOptionClick = onOptionClick,
        )

    }
}