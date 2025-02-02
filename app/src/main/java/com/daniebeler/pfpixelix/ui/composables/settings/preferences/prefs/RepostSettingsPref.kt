package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPref


@Composable
fun RepostSettingsPref(openUrl: () -> Unit) {
    SettingPref(
        leadingIcon = R.drawable.sync_outline,
        title = stringResource(id = R.string.repost_settings),
        trailingContent = R.drawable.chevron_back_outline,
        onClick = openUrl
    )
}

@Preview
@Composable
private fun RepostSettingsPrefPreview() {
    RepostSettingsPref(openUrl = {
        println("URL opened: url")
    })
}
