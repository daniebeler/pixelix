package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPref

@Composable
fun MoreSettingsPref(openUrl: () -> Unit) {
    SettingPref(
        leadingIcon = Res.drawable.settings_outline,
        title = stringResource(Res.string.more_settings),
        trailingContent = Res.drawable.open_outline,
        onClick = openUrl
    )
}

@Preview
@Composable
private fun MoreSettingsPrefPreview() {
    MoreSettingsPref(openUrl = {
        println("URL opened: url")
    })
}
