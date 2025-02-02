package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPrefUtil
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SwitchIntPref

object FocusModePrefUtil {
    const val KEY_FOCUS_MODE = "focus_mode"

    fun isEnable(context: Context): Boolean {
        return SettingPrefUtil.getValue(
            context, KEY_FOCUS_MODE, SettingPrefUtil.OFF
        ) == SettingPrefUtil.ON
    }
}

@Preview
@Composable
fun FocusModePref() {
    val context = LocalContext.current

    SwitchIntPref(
        key = FocusModePrefUtil.KEY_FOCUS_MODE,
        leadingIcon =  R.drawable.square_outline,
        title = stringResource(R.string.focus_mode),
        default = SettingPrefUtil.OFF,
        onCheckedChange = {
            SettingPrefUtil.setValue(context,
                FocusModePrefUtil.KEY_FOCUS_MODE, it)
        }
    )
}