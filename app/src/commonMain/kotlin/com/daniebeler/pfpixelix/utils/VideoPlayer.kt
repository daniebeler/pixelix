package com.daniebeler.pfpixelix.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope

expect class VideoPlayer(
    context: KmpContext,
    coroutineScope: CoroutineScope
) {
    var progress: ((current: Long, duration: Long) -> Unit)?
    var hasAudio: ((Boolean) -> Unit)?

    @Composable
    fun view(modifier: Modifier)

    fun prepare(url: String)
    fun play()
    fun pause()
    fun release()
    fun audio(enable: Boolean)
}