package com.daniebeler.pfpixelix.utils

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

actual class VideoPlayer actual constructor(
    context: KmpContext,
    coroutineScope: CoroutineScope
) {
    actual var progress: ((current: Long, duration: Long) -> Unit)? = null
    actual var hasAudio: ((Boolean) -> Unit)? = null

    private val audioAttributes =
        AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()
    private val player = ExoPlayer.Builder(context).build().apply {
        repeatMode = Player.REPEAT_MODE_ONE
        setAudioAttributes(audioAttributes, false)
        addListener(object : Player.Listener {
            @UnstableApi
            override fun onTracksChanged(tracks: Tracks) {
                tracks.groups.forEach { trackGroup ->
                    trackGroup.mediaTrackGroup.let { mediaTrackGroup ->
                        for (i in 0 until mediaTrackGroup.length) {
                            val format = mediaTrackGroup.getFormat(i)
                            if (format.sampleMimeType?.startsWith("audio/") == true) {
                                hasAudio?.invoke(true)
                                break
                            }
                        }
                    }
                }
            }
        })
    }

    init {
        coroutineScope.launch {
            while (isActive) {
                val duration = player.contentDuration
                val currentTime = player.currentPosition
                if (duration > 0 && currentTime <= duration) {
                    progress?.invoke(currentTime, duration)
                }
                delay(300)
            }
        }
    }

    @OptIn(UnstableApi::class)
    @Composable
    actual fun view(modifier: Modifier) {
        AndroidView(
            modifier = modifier,
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = this@VideoPlayer.player
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                    setShowPreviousButton(false)
                    useController = false
                }
            }
        )
    }

    actual fun prepare(url: String) {
        val item = MediaItem.fromUri(url)
        player.setMediaItem(item)
        player.prepare()
    }

    actual fun play() {
        player.play()
    }

    actual fun pause() {
        player.pause()
    }

    actual fun release() {
        player.release()
    }

    actual fun audio(enable: Boolean) {
        player.volume = if (enable) 1f else 0f
        player.setAudioAttributes(audioAttributes, enable)
    }
}