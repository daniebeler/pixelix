package com.daniebeler.pfpixelix.ui.composables.settings.preferences

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.utils.globalContext

object HideSensitiveContentPrefUtil {
    const val KEY_ICON_VISUAL_EFFECTS = "hide_sensitive_content"

    fun isEnable(context: Context): Boolean {
        return SettingPrefUtil.getValue(
            context, KEY_ICON_VISUAL_EFFECTS, SettingPrefUtil.ON
        ) == SettingPrefUtil.ON
    }
}

@Composable
fun HideSensitiveContentPref() {
    SwitchIntPref(
        key = HideSensitiveContentPrefUtil.KEY_ICON_VISUAL_EFFECTS,
        leadingIcon = Icons.Rounded.Animation,
        title = stringResource(R.string.hide_sensitive_content),
        default = SettingPrefUtil.OFF,
        onCheckedChange = {
            SettingPrefUtil.setValue(globalContext, HideSensitiveContentPrefUtil.KEY_ICON_VISUAL_EFFECTS, it)
        }
    )
}