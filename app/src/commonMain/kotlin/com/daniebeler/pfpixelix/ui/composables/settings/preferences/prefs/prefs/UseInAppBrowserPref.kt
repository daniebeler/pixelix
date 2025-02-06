package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.compose.runtime.Composable
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPrefUtil
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SwitchIntPref
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import org.jetbrains.compose.resources.stringResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.browsers_outline
import pixelix.app.generated.resources.use_in_app_browser

object UseInAppBrowserPrefUtil {
    const val KEY_USE_IN_APP_BROWSER = "focus_mode"

    fun isEnable(context: KmpContext): Boolean {
        return SettingPrefUtil.getValue(
            context, KEY_USE_IN_APP_BROWSER, SettingPrefUtil.ON
        ) == SettingPrefUtil.ON
    }
}

@Composable
fun UseInAppBrowserPref() {
    val context = LocalKmpContext.current

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