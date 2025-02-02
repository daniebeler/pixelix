package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPrefUtil
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SwitchIntPref

object HideSensitiveContentPrefUtil {
    const val KEY_HIDE_SESITIVE_CONTENT = "hide_sensitive_content"

    fun isEnable(context: Context): Boolean {
        return SettingPrefUtil.getValue(
            context, KEY_HIDE_SESITIVE_CONTENT, SettingPrefUtil.ON
        ) == SettingPrefUtil.ON
    }
}

@Preview
@Composable
fun HideSensitiveContentPref() {
    val context = LocalContext.current

    SwitchIntPref(
        key = HideSensitiveContentPrefUtil.KEY_HIDE_SESITIVE_CONTENT,
        leadingIcon = R.drawable.eye_off_outline,
        title = stringResource(R.string.hide_sensitive_content),
        default = SettingPrefUtil.ON,
        onCheckedChange = {
            SettingPrefUtil.setValue(context,
                HideSensitiveContentPrefUtil.KEY_HIDE_SESITIVE_CONTENT, it)
        }
    )
}