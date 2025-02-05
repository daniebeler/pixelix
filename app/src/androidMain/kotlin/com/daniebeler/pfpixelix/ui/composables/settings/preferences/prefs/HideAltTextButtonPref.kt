package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*
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
        leadingIcon =  Res.drawable.document_text_outline,
        title = stringResource(Res.string.hide_alt_text_button),
        default = SettingPrefUtil.OFF,
        onCheckedChange = {
            SettingPrefUtil.setValue(context,
                HideAltTextButtonPrefUtil.KEY_HIDE_ALT_TEXT_BUTTON, it)
        }
    )
}