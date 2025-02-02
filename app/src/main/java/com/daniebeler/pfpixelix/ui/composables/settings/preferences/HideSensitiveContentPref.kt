package com.daniebeler.pfpixelix.ui.composables.settings.preferences

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniebeler.pfpixelix.R

object HideSensitiveContentPrefUtil {
    const val KEY_ICON_VISUAL_EFFECTS = "hide_sensitive_content"

    fun isEnable(context: Context): Boolean {
        return SettingPrefUtil.getValue(
            context, KEY_ICON_VISUAL_EFFECTS, SettingPrefUtil.ON
        ) == SettingPrefUtil.ON
    }
}

@Preview
@Composable
fun HideSensitiveContentPref() {
    val context = LocalContext.current

    SwitchIntPref(
        key = HideSensitiveContentPrefUtil.KEY_ICON_VISUAL_EFFECTS,
        leadingIcon = Icons.Rounded.Animation,
        title = stringResource(R.string.hide_sensitive_content),
        default = SettingPrefUtil.OFF,
        onCheckedChange = {
            SettingPrefUtil.setValue(context, HideSensitiveContentPrefUtil.KEY_ICON_VISUAL_EFFECTS, it)
        }
    )
}