package com.daniebeler.pfpixelix.utils

import android.content.Context
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.imeAwareInsets(context: Context, substractHeight: Dp): Modifier {
    val imeInsets = WindowInsets.ime.asPaddingValues(Density(context)).calculateBottomPadding()
    val bottomPadding = if (imeInsets > substractHeight) {
        imeInsets.minus(substractHeight)
    } else {
        0.dp
    }
    return this then Modifier.padding(bottom = bottomPadding)
}