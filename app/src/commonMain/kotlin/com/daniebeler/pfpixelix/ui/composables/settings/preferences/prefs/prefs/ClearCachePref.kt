package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.daniebeler.pfpixelix.di.injectViewModel
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPref
import org.jetbrains.compose.resources.stringResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.clear_cache
import pixelix.app.generated.resources.save_outline

@Composable
fun ClearCachePref(drawerState: DrawerState) {
    val viewModel = injectViewModel("ClearCacheViewModel") { clearCacheViewModel }
    val cacheSize = remember { mutableStateOf("") }

    LaunchedEffect(drawerState.isOpen) {
        cacheSize.value = humanReadableByteCountSI(viewModel.getCacheSizeInBytes())
    }

    SettingPref(
        leadingIcon = Res.drawable.save_outline,
        title = stringResource(Res.string.clear_cache),
        desc = cacheSize.value,
        trailingContent = null,
        onClick = {
            viewModel.cleanCache()
            cacheSize.value = humanReadableByteCountSI(viewModel.getCacheSizeInBytes())
        }
    )
}

private fun humanReadableByteCountSI(bytes: Long): String {
    var bytes = bytes
    if (-1000 < bytes && bytes < 1000) {
        return "$bytes B"
    }
    val chars = "kMGTPE".toCharArray()
    var ci = 0
    while (bytes <= -999950 || bytes >= 999950) {
        bytes /= 1000
        ci++
    }

    val valueRounded = (bytes / 100.0).toInt() / 10.0 // Round down to one decimal place
    return "$valueRounded ${chars[ci]}B"
}
