package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPref
import java.text.CharacterIterator
import java.text.StringCharacterIterator

@Preview
@Composable
fun ClearCachePref() {
    val context = LocalContext.current
    val cacheSize = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        cacheSize.value = getCacheSize(context)
    }

    SettingPref(
        leadingIcon = Res.drawable.save_outline,
        title = stringResource(Res.string.clear_cache),
        desc = cacheSize.value,
        trailingContent = null,
        onClick = {cacheSize.value = deleteCache(context)}
    )
}

private fun getCacheSize(context: Context): String {
    val cacheInbytes: Long =
        context.cacheDir.walkBottomUp().fold(0L, { acc, file -> acc + file.length() })

    return humanReadableByteCountSI(cacheInbytes)
}

private fun humanReadableByteCountSI(bytes: Long): String {
    var bytes = bytes
    if (-1000 < bytes && bytes < 1000) {
        return "$bytes B"
    }
    val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
    while (bytes <= -999950 || bytes >= 999950) {
        bytes /= 1000
        ci.next()
    }
    return String.format("%.1f %cB", bytes / 1000.0, ci.current())
}

private fun deleteCache(context: Context): String {
    context.cacheDir.deleteRecursively()
    return getCacheSize(context)
}