package com.daniebeler.pfpixelix.ui.composables.post

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.utils.LocalKmpContext
import com.daniebeler.pfpixelix.utils.VideoPlayer

@Composable
fun VideoAttachment(
    attachment: MediaAttachment,
    viewModel: PostViewModel,
    onReady: () -> Unit
) {
    val context = LocalKmpContext.current
    val coroutineScope = rememberCoroutineScope()
    val player = remember { VideoPlayer(context, coroutineScope) }
    var progress by remember { mutableFloatStateOf(0f) }
    var hasAudio by remember { mutableStateOf(false) }

    var videoFrameIsVisible by remember { mutableStateOf(false) }

    Column {
        Box {
            player.view(
                modifier = Modifier
                    .fillMaxWidth()
                    .run {
                        val aspect = attachment.meta?.original?.aspect?.toFloat()
                        if (aspect != null) aspectRatio(aspect) else this
                    }
                    .isVisible(threshold = 50) { videoFrameIsVisible = it }
            )
            if (hasAudio) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    onClick = {
                        viewModel.toggleVolume(!viewModel.volume)
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors()
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
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            trackColor = MaterialTheme.colorScheme.background
        )
    }

    LaunchedEffect(attachment) {
        player.prepare(attachment.url.orEmpty())
    }
    val started = progress > 0
    LaunchedEffect(started) { onReady() }

    LaunchedEffect(viewModel.volume) {
        player.audio(viewModel.volume)
    }

    DisposableEffect(Unit) {
        player.progress = { current, duration ->
            progress = current.toFloat() / duration.toFloat()
        }
        player.hasAudio = { hasAudio = it }

        onDispose {
            player.progress = null
            player.hasAudio = null
            player.release()
        }
    }

    LaunchedEffect(videoFrameIsVisible) {
        if (videoFrameIsVisible) {
            player.play()
        } else {
            player.pause()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (videoFrameIsVisible) {
                        player.play()
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    player.pause()
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

}
