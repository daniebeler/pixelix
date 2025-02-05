package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPrefUtil
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SwitchIntPref
import com.daniebeler.pfpixelix.utils.KmpContext

actual object HideSensitiveContentPrefUtil {
    const val KEY_HIDE_SESITIVE_CONTENT = "hide_sensitive_content"

    actual fun isEnable(context: KmpContext): Boolean {
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
        leadingIcon = Res.drawable.eye_off_outline,
        title = stringResource(Res.string.hide_sensitive_content),
        default = SettingPrefUtil.ON,
        onCheckedChange = {
            SettingPrefUtil.setValue(context,
                HideSensitiveContentPrefUtil.KEY_HIDE_SESITIVE_CONTENT, it)
        }
    )
}