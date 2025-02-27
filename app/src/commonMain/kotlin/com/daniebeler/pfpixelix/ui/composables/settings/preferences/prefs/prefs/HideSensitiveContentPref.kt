package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.daniebeler.pfpixelix.di.LocalAppComponent
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SwitchPref
import org.jetbrains.compose.resources.stringResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.eye_off_outline
import pixelix.app.generated.resources.hide_sensitive_content

@Composable
fun HideSensitiveContentPref() {
    val prefs = LocalAppComponent.current.preferences
    val state = remember { mutableStateOf(prefs.hideSensitiveContent) }
    LaunchedEffect(state.value) {
        prefs.hideSensitiveContent = state.value
    }
    SwitchPref(
        leadingIcon =  Res.drawable.eye_off_outline,
        title = stringResource(Res.string.hide_sensitive_content),
        state = state
    )
}