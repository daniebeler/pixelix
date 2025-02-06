package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.compose.runtime.Composable
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPrefUtil
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SwitchIntPref
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import org.jetbrains.compose.resources.stringResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.focus_mode
import pixelix.app.generated.resources.square_outline

object FocusModePrefUtil {
    const val KEY_FOCUS_MODE = "focus_mode"

    fun isEnable(context: KmpContext): Boolean {
        return SettingPrefUtil.getValue(
            context, KEY_FOCUS_MODE, SettingPrefUtil.OFF
        ) == SettingPrefUtil.ON
    }
}

@Composable
fun FocusModePref() {
    val context = LocalKmpContext.current

    SwitchIntPref(
        key = FocusModePrefUtil.KEY_FOCUS_MODE,
        leadingIcon =  Res.drawable.square_outline,
        title = stringResource(Res.string.focus_mode),
        default = SettingPrefUtil.OFF,
        onCheckedChange = {
            SettingPrefUtil.setValue(context,
                FocusModePrefUtil.KEY_FOCUS_MODE, it)
        }
    )
}