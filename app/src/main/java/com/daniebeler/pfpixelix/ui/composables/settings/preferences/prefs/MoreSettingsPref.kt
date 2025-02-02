package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPref

@Composable
fun MoreSettingsPref(openUrl: () -> Unit) {
    SettingPref(
        leadingIcon = R.drawable.settings_outline,
        title = stringResource(id = R.string.more_settings),
        trailingContent = R.drawable.chevron_back_outline,
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
