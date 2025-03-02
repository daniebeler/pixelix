package com.daniebeler.pfpixelix.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.AVFoundation.AVLayerVideoGravityResize
import platform.AVFoundation.AVMediaTypeAudio
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.asset
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.muted
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.tracksWithMediaType
import platform.AVKit.AVPlayerViewController
import platform.CoreMedia.CMTimeGetSeconds
import platform.Foundation.NSURL
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
actual class VideoPlayer actual constructor(
    context: KmpContext,
    private val coroutineScope: CoroutineScope
) {
    actual var progress: ((current: Long, duration: Long) -> Unit)? = null
    actual var hasAudio: ((Boolean) -> Unit)? = null

    private var extractAudioInfoJob: Job? = null

    private val player = AVPlayer()
    private val playerViewController = AVPlayerViewController().apply {
        this.player = this@VideoPlayer.player
        this.videoGravity = AVLayerVideoGravityResize
        this.showsPlaybackControls = false
        this.view.userInteractionEnabled = false
    }
    private val playerLayer = AVPlayerLayer().apply {
        this.player = playerViewController.player
    }

    init {
        coroutineScope.launch {
            while (isActive) {
                player.currentItem?.let {
                    val duration = CMTimeGetSeconds(it.duration()).toLong()
                    val currentTime = CMTimeGetSeconds(it.currentTime()).toLong()
                    if (duration > 0 && currentTime <= duration) {
                        progress?.invoke(currentTime, duration)
                    }
                }
                delay(300)
            }
        }
    }

    @Composable
    actual fun view(modifier: Modifier) {
        UIKitView(
            modifier = modifier,
            factory = {
                UIView().apply {
                    playerLayer.setFrame(frame)
                    playerViewController.view.setFrame(frame)
                    addSubview(playerViewController.view)
                }
            },
            update = { view: UIView ->
                playerLayer.setFrame(view.frame)
                playerViewController.view.setFrame(view.frame)
            }
        )
    }

    actual fun prepare(url: String) {
        release()

        val item = AVPlayerItem(NSURL(string = url))
        player.replaceCurrentItemWithPlayerItem(item)

        extractAudioInfoJob = coroutineScope.launch {
            val withAudio = withContext(Dispatchers.Default) {
                item.asset.tracksWithMediaType(AVMediaTypeAudio).isNotEmpty()
            }
            hasAudio?.invoke(withAudio)
        }
    }

    actual fun play() {
        player.play()
    }
    actual fun pause() {
        player.pause()
    }
    actual fun release() {
        player.replaceCurrentItemWithPlayerItem(null)
        extractAudioInfoJob?.cancel()
    }
    actual fun audio(enable: Boolean) {
        player.muted = !enable
    }
}