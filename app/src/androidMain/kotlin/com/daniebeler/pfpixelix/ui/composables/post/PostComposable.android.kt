package com.daniebeler.pfpixelix.ui.composables.post

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.daniebeler.pfpixelix.utils.KmpUri
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
actual fun VideoPlayer(
    uri: KmpUri,
    viewModel: PostViewModel,
    onSuccess: () -> Unit
) {
    val context = LocalKmpContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    var hasAudio by remember { mutableStateOf(false) }

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                @OptIn(UnstableApi::class)
                override fun onTracksChanged(tracks: Tracks) {
                    tracks.groups.forEach { trackGroup ->
                        trackGroup.mediaTrackGroup.let { mediaTrackGroup ->
                            for (i in 0 until mediaTrackGroup.length) {
                                val format = mediaTrackGroup.getFormat(i)
                                if (format.sampleMimeType?.startsWith("audio/") == true) {
                                    hasAudio = true
                                    break
                                }
                            }
                        }
                    }
                }
            })
            addListener(object : Player.Listener {
                override fun onIsLoadingChanged(isLoading: Boolean) {
                    if (!isLoading) {
                        onSuccess()
                    }
                }
            })
        }
    }

    var visible by remember {
        mutableStateOf(false)
    }
    val audioAttributes =
        AudioAttributes.Builder().setUsage(C.USAGE_MEDIA).setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()
    exoPlayer.setAudioAttributes(audioAttributes, true)

    val contentLength = remember {
        mutableLongStateOf(1.toLong())
    }

    val currentPos = remember {
        mutableLongStateOf(0.toLong())
    }

    val currentProgress = remember {
        mutableFloatStateOf(0.toFloat())
    }

    val mediaSource = remember(uri) {
        MediaItem.fromUri(uri)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.play()
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }


    LaunchedEffect(Unit) {
        while (true) {
            contentLength.longValue =
                if (exoPlayer.contentDuration > 0) exoPlayer.contentDuration else 1
            currentPos.longValue =
                if (exoPlayer.currentPosition > 0) exoPlayer.currentPosition else 0
            currentProgress.floatValue = currentPos.longValue.toFloat() / contentLength.longValue
            delay(10)
        }
    }

    // Set MediaSource to ExoPlayer
    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    // Manage lifecycle events
    DisposableEffect(Unit) {
        exoPlayer.volume = if (viewModel.volume) {
            1f
        } else {
            0f
        }
        onDispose {
            exoPlayer.release()
        }
    }
    Column {
        Box(Modifier.isVisible(threshold = 50) {
            if (visible != it) {
                visible = it
                if (visible) {
                    exoPlayer.play()
                } else {
                    exoPlayer.pause()
                }
            }
        }) {
            AndroidView(factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                    setShowPreviousButton(false)
                    useController = false
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { })
            DisposableEffect(key1 = Unit, effect = {
                onDispose {
                    exoPlayer.release()
                }
            })

            if (hasAudio) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp), onClick = {
                        viewModel.toggleVolume(!viewModel.volume)
                        exoPlayer.volume = if (viewModel.volume) {
                            1f
                        } else {
                            0f
                        }
                    }, colors = IconButtonDefaults.filledTonalIconButtonColors()
                ) {
                    if (viewModel.volume) {
                        Icon(
                            Icons.AutoMirrored.Outlined.VolumeUp,
                            contentDescription = "Volume on",
                            Modifier.size(18.dp)
                        )
                    } else {
                        Icon(
                            Icons.AutoMirrored.Outlined.VolumeOff,
                            contentDescription = "Volume off",
                            Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        LinearProgressIndicator(
            progress = { currentProgress.floatValue },
            modifier = Modifier.fillMaxWidth(),
            trackColor = MaterialTheme.colorScheme.background
        )
    }


    //exoPlayer.playWhenReady = true
    //exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
}