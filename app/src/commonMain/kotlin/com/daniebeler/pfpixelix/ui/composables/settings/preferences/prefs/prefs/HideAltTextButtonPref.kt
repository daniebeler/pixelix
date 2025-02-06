package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.compose.runtime.Composable
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPrefUtil
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SwitchIntPref
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import org.jetbrains.compose.resources.stringResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.document_text_outline
import pixelix.app.generated.resources.hide_alt_text_button

object HideAltTextButtonPrefUtil {
    const val KEY_HIDE_ALT_TEXT_BUTTON = "hide_alt_text_button"

    fun isEnable(context: KmpContext): Boolean {
        return SettingPrefUtil.getValue(
            context, KEY_HIDE_ALT_TEXT_BUTTON, SettingPrefUtil.OFF
        ) == SettingPrefUtil.ON
    }
}

@Composable
fun HideAltTextButtonPref() {
    val context = LocalKmpContext.current

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