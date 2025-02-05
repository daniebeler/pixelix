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

object UseInAppBrowserPrefUtil {
    const val KEY_USE_IN_APP_BROWSER = "focus_mode"

    fun isEnable(context: Context): Boolean {
        return SettingPrefUtil.getValue(
            context, KEY_USE_IN_APP_BROWSER, SettingPrefUtil.ON
        ) == SettingPrefUtil.ON
    }
}

@Preview
@Composable
fun UseInAppBrowserPref() {
    val context = LocalContext.current

    SwitchIntPref(
        key = UseInAppBrowserPrefUtil.KEY_USE_IN_APP_BROWSER,
        leadingIcon =  Res.drawable.browsers_outline,
        title = stringResource(Res.string.use_in_app_browser),
        default = SettingPrefUtil.ON,
        onCheckedChange = {
            SettingPrefUtil.setValue(context,
                UseInAppBrowserPrefUtil.KEY_USE_IN_APP_BROWSER, it)
        }
    )
}