package com.daniebeler.pfpixelix.ui.composables.post

import androidx.compose.runtime.Composable
import com.daniebeler.pfpixelix.utils.KmpUri

@Composable
actual fun VideoPlayer(
    uri: KmpUri,
    viewModel: PostViewModel,
    onSuccess: () -> Unit
) {
}