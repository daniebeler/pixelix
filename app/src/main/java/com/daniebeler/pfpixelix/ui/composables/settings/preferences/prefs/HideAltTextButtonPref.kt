package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPrefUtil
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SwitchIntPref

object HideAltTextButtonPrefUtil {
    const val KEY_HIDE_ALT_TEXT_BUTTON = "hide_alt_text_button"

    fun isEnable(context: Context): Boolean {
        return SettingPrefUtil.getValue(
            context, KEY_HIDE_ALT_TEXT_BUTTON, SettingPrefUtil.OFF
        ) == SettingPrefUtil.ON
    }
}

@Preview
@Composable
fun HideAltTextButtonPref() {
    val context = LocalContext.current

    SwitchIntPref(
        key = HideAltTextButtonPrefUtil.KEY_HIDE_ALT_TEXT_BUTTON,
        leadingIcon =  R.drawable.document_text_outline,
        title = stringResource(R.string.hide_alt_text_button),
        default = SettingPrefUtil.OFF,
        onCheckedChange = {
            SettingPrefUtil.setValue(context,
                HideAltTextButtonPrefUtil.KEY_HIDE_ALT_TEXT_BUTTON, it)
        }
    )
}