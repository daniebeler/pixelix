package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorComposable(message: String) {

    if (message.isNotBlank()) {
        Text(text = message, Modifier.fillMaxSize().wrapContentSize(
            Alignment.Center))
    }
}