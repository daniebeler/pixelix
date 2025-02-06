package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.compose.runtime.Composable
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPrefUtil
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SwitchIntPref
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import org.jetbrains.compose.resources.stringResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.eye_off_outline
import pixelix.app.generated.resources.hide_sensitive_content

object HideSensitiveContentPrefUtil {
    const val KEY_HIDE_SESITIVE_CONTENT = "hide_sensitive_content"

    fun isEnable(context: KmpContext): Boolean {
        return SettingPrefUtil.getValue(
            context, KEY_HIDE_SESITIVE_CONTENT, SettingPrefUtil.ON
        ) == SettingPrefUtil.ON
    }
}

@Composable
fun HideSensitiveContentPref() {
    val context = LocalKmpContext.current

    SwitchIntPref(
        key = HideSensitiveContentPrefUtil.KEY_HIDE_SESITIVE_CONTENT,
        leadingIcon = Res.drawable.eye_off_outline,
        title = stringResource(Res.string.hide_sensitive_content),
        default = SettingPrefUtil.ON,
        onCheckedChange = {
            SettingPrefUtil.setValue(context,
                HideSensitiveContentPrefUtil.KEY_HIDE_SESITIVE_CONTENT, it)
        }
    )
}