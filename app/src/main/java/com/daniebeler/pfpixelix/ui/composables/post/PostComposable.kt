package com.daniebeler.pfpixelix.ui.composables.post

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Cached
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.ui.composables.hashtagMentionText.HashtagsMentionsTextView
import com.daniebeler.pfpixelix.ui.composables.states.LoadingComposable
import com.daniebeler.pfpixelix.utils.BlurHashDecoder
import com.daniebeler.pfpixelix.utils.Navigate
import com.daniebeler.pfpixelix.utils.zoomable.rememberZoomState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.snapBackZoomable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostComposable(
    post: Post,
    navController: NavController,
    postGetsDeleted: (postId: String) -> Unit,
    setZindex: (zIndex: Float) -> Unit,
    viewModel: PostViewModel = hiltViewModel(key = "post" + post.id)
) {

    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableIntStateOf(0) }

    DisposableEffect(post.createdAt) {
        viewModel.convertTime(post.createdAt)
        onDispose {}
    }

    LaunchedEffect(Unit) {
        if (viewModel.post == null) {
            viewModel.updatePost(post)
        }
    }

    LaunchedEffect(viewModel.deleteState.deleted) {
        if (viewModel.deleteState.deleted) {
            postGetsDeleted(post.id)
        }
    }

    LaunchedEffect(post) {
        if (viewModel.post == null || viewModel.post!!.copy() != post.copy()) {
            viewModel.updatePost(post)
        }
    }

    val mediaAttachmentsCount = post.mediaAttachments.count()

    val pagerState = rememberPagerState(pageCount = { mediaAttachmentsCount })

    if (viewModel.post != null) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 16.dp, end = 12.dp)
                    .clickable(onClick = {
                        Navigate.navigate(
                            "profile_screen/" + viewModel.post!!.account.id, navController
                        )
                    })
            ) {
                AsyncImage(
                    model = viewModel.post!!.account.avatar,
                    contentDescription = "",
                    modifier = Modifier
                        .height(36.dp)
                        .width(36.dp)
                        .clip(CircleShape)
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
//                    Text(
//                        text = viewModel.post!!.account.displayname ?: "",
//                        fontWeight = FontWeight.Bold,
//                        lineHeight = 10.sp
//                    )
                    Text(
                        text = viewModel.post!!.account.acct,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (viewModel.post!!.place != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = "",
                                modifier = Modifier.height(20.dp)
                            )
                            Row {
                                Text(text = viewModel.post!!.place?.name ?: "", fontSize = 12.sp)
                                if (post.place?.country != null) {
                                    Text(
                                        text = ", " + (viewModel.post!!.place?.country ?: ""),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = {
                    showBottomSheet = 2
                }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreHoriz,
                        modifier = Modifier.size(32.dp),
                        contentDescription = ""
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (viewModel.post!!.sensitive && !viewModel.showPost) {

                Box {
                    val blurHashAsDrawable = BlurHashDecoder.blurHashBitmap(
                        LocalContext.current.resources,
                        viewModel.post!!.mediaAttachments[0].blurHash
                    )

                    if (blurHashAsDrawable.bitmap != null) {
                        Image(
                            blurHashAsDrawable.bitmap.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.aspectRatio(
                                viewModel.post!!.mediaAttachments[0].meta?.original?.aspect?.toFloat()
                                    ?: 1.5f
                            )
                        )
                    }


                    Column(
                        Modifier.aspectRatio(
                            viewModel.post!!.mediaAttachments[0].meta?.original?.aspect?.toFloat()
                                ?: 1.5f
                        ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        if (viewModel.post!!.spoilerText.isNotEmpty()) {
                            Text(text = viewModel.post!!.spoilerText)
                        } else {
                            Text(text = "This post may contain sensitive content.")
                        }


                        Button(onClick = {
                            viewModel.toggleShowPost()
                        }) {
                            Text(text = "Show post")
                        }
                    }
                }

            } else {
                if (viewModel.post!!.mediaAttachments.count() > 1) {
                    Box(

                    ) {
                        HorizontalPager(
                            state = pagerState, modifier = Modifier.zIndex(50f)
                        ) { page ->
                            Box(
                                modifier = Modifier
                                    .zIndex(10f)
                                    .padding(start = 12.dp, end = 12.dp)
                            ) {
                                PostImage(
                                    mediaAttachment = viewModel.post!!.mediaAttachments[page],
                                    viewModel.post!!.id,
                                    setZindex = { setZindex(it) },
                                    viewModel
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .zIndex(51f)
                                .padding(top = 16.dp, end = 28.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                                .padding(vertical = 3.dp, horizontal = 12.dp)
                        ) {
                            Text(
                                text = (pagerState.currentPage + 1).toString() + "/" + viewModel.post!!.mediaAttachments.count(),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(8.dp)
                            )
                        }
                    }
                } else if (viewModel.post != null && viewModel.post!!.mediaAttachments.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .zIndex(10f)
                            .padding(start = 12.dp, end = 12.dp)
                    ) {
                        PostImage(
                            mediaAttachment = viewModel.post!!.mediaAttachments[0],
                            viewModel.post!!.id,
                            setZindex = { setZindex(it) },
                            viewModel
                        )
                    }
                }
            }

            Column(Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (viewModel.post!!.favourited) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable {
                                        viewModel.unlikePost(viewModel.post!!.id)
                                    },
                                contentDescription = "",
                                tint = Color(0xFFDD2E44)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable {
                                        viewModel.likePost(viewModel.post!!.id)
                                    },
                                contentDescription = ""
                            )

                        }

                        Spacer(Modifier.width(4.dp))

                        Text(
                            text = viewModel.post!!.favouritesCount.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.width(32.dp))

                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    viewModel.loadReplies(
                                        viewModel.post!!.account.id, viewModel.post!!.id
                                    )
                                    showBottomSheet = 1
                                },
                            contentDescription = ""
                        )

                        Spacer(Modifier.width(4.dp))

                        Text(
                            text = viewModel.post!!.replyCount.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )


                    }

                    Row {

                        if (viewModel.post!!.reblogged) {
                            IconButton(onClick = {
                                viewModel.unreblogPost(viewModel.post!!.id)
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Cached,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            IconButton(onClick = {
                                viewModel.reblogPost(viewModel.post!!.id)
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Cached, contentDescription = ""
                                )
                            }
                        }

                        Spacer(Modifier.width(14.dp))

                        if (viewModel.post!!.bookmarked) {
                            IconButton(onClick = {
                                viewModel.unBookmarkPost(post.id)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Bookmark, contentDescription = ""
                                )
                            }
                        } else {
                            IconButton(onClick = {
                                viewModel.bookmarkPost(post.id)
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.BookmarkBorder,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }

                Row {
                    if (post.likedBy?.username?.isNotBlank() == true) {
                        Text(text = stringResource(id = R.string.liked_by) + " ", fontSize = 14.sp)
                        Text(text = post.likedBy.username,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                Navigate.navigate(
                                    "profile_screen/" + post.likedBy.id, navController
                                )
                            })
                        if (post.favouritesCount > 1) {
                            Text(
                                text = " " + stringResource(id = R.string.and) + " ",
                                fontSize = 14.sp
                            )
                            Text(text = (post.favouritesCount - 1).toString() + " " + stringResource(
                                id = R.string.others
                            ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.clickable {
                                    viewModel.loadLikedBy(post.id)
                                    showBottomSheet = 3
                                })
                        }
                    } else {
                        Text(text = stringResource(id = R.string.no_likes_yet), fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (viewModel.post!!.content.isNotBlank()) {
                    HashtagsMentionsTextView(text = viewModel.post!!.content,
                        mentions = viewModel.post!!.mentions,
                        navController = navController,
                        openUrl = { url -> viewModel.openUrl(context, url) })
                }

                if (viewModel.post!!.replyCount > 0) {

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = stringResource(
                        R.string.view_comments, viewModel.post!!.replyCount
                    ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable {
                            viewModel.loadReplies(viewModel.post!!.account.id, viewModel.post!!.id)
                            showBottomSheet = 1
                        })
                }

                Text(
                    text = viewModel.timeAgoString,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showBottomSheet > 0) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = 0
            },
            sheetState = sheetState,
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            if (showBottomSheet == 1) {
                CommentsBottomSheet(post, navController, viewModel)
            } else if (showBottomSheet == 2) {
                if (viewModel.myAccountId != null && post.account.id == viewModel.myAccountId) {
                    ShareBottomSheet(
                        context,
                        post.url,
                        true,
                        viewModel,
                        post,
                        pagerState.currentPage,
                        navController
                    )
                } else {
                    ShareBottomSheet(
                        context,
                        post.url,
                        false,
                        viewModel,
                        post,
                        pagerState.currentPage,
                        navController
                    )
                }
            } else if (showBottomSheet == 3) {
                LikesBottomSheet(viewModel, navController)
            }
        }
    }
    if (viewModel.deleteDialog != null) {
        AlertDialog(icon = {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
        }, title = {
            Text(text = stringResource(R.string.delete_post))
        }, text = {
            Text(text = stringResource(R.string.this_action_cannot_be_undone))
        }, onDismissRequest = {
            viewModel.deleteDialog = null
        }, confirmButton = {
            TextButton(onClick = {
                viewModel.deletePost(viewModel.deleteDialog!!)
            }) {
                Text(stringResource(R.string.delete))
            }
        }, dismissButton = {
            TextButton(onClick = {
                viewModel.deleteDialog = null
            }) {
                Text(stringResource(id = R.string.cancel))
            }
        })

    }
    LoadingComposable(isLoading = viewModel.deleteState.isLoading)
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun PostImage(
    mediaAttachment: MediaAttachment,
    postId: String,
    setZindex: (zIndex: Float) -> Unit,
    viewModel: PostViewModel
) {
    var showHeart by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(if (showHeart) 1f else 0f, label = "heart animation")
    LaunchedEffect(showHeart) {
        if (showHeart) {
            delay(1000)
            showHeart = false
        }
    }

    var altText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(80f)
            .clip(RoundedCornerShape(16.dp))
    ) {

        val blurHashAsDrawable = BlurHashDecoder.blurHashBitmap(
            LocalContext.current.resources,
            mediaAttachment.blurHash,
        )

        if (blurHashAsDrawable.bitmap != null) {
            Image(
                blurHashAsDrawable.bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(
                    mediaAttachment.meta?.original?.aspect?.toFloat() ?: 1f
                )
            )
        }

        val zoomState = rememberZoomState()

        val showAltTextIcon = remember {
            mutableStateOf(true)
        }

        if (zoomState.scale != 1f) {
            setZindex(100f)
            showAltTextIcon.value = false
        } else {
            setZindex(1f)
            showAltTextIcon.value = true
        }

        Box(modifier = Modifier
            .zIndex(2f)
            .snapBackZoomable(zoomState)
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    CoroutineScope(Dispatchers.Default).launch {
                        viewModel.likePost(postId)
                        showHeart = true
                    }
                })
            }) {
            if (mediaAttachment.type == "image" && mediaAttachment.url?.takeLast(4) != ".gif" && mediaAttachment.url?.takeLast(
                    5
                ) != ".webp"
            ) {
                ImageWrapper(
                    mediaAttachment
                ) { zoomState.setContentSize(it.painter.intrinsicSize) }
            } else if (mediaAttachment.url?.takeLast(4) == ".gif" || mediaAttachment.url?.takeLast(5) == ".webp") {
                GifPlayer(mediaAttachment)
            } else {
                VideoPlayer(
                    uri = Uri.parse(mediaAttachment.url),
                    mediaAttachment = mediaAttachment,
                    viewModel
                )
            }
        }

        if (mediaAttachment.description?.isNotBlank() == true && showAltTextIcon.value && !viewModel.isAltTextButtonHidden) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .zIndex(3f)
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                    .clickable {
                        altText = mediaAttachment.description
                    }
                    .padding(10.dp),
            ) {
                Icon(
                    Icons.Outlined.Description,
                    contentDescription = "Show alt text",
                    Modifier.size(22.dp)
                )
            }
        }



        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = Color(0xFFDD2E44),
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.Center)
                .scale(scale.value)
                .zIndex(100f)
        )

        if (altText.isNotBlank()) {
            AlertDialog(title = {
                Text(text = stringResource(R.string.media_description))
            }, text = {
                Text(text = altText)
            }, onDismissRequest = {
                altText = ""
            }, confirmButton = {
                TextButton(onClick = {
                    altText = ""
                }) {
                    Text(stringResource(id = android.R.string.ok))
                }
            })
        }

    }
}

@Composable
private fun ImageWrapper(
    mediaAttachment: MediaAttachment,
    setContentSize: (painter: AsyncImagePainter.State.Success) -> Unit
) {

    AsyncImage(model = mediaAttachment.url,
        contentDescription = "",
        Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth,
        onSuccess = { state ->
            setContentSize(state)
        })
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun GifPlayer(mediaAttachment: MediaAttachment) {
    GlideImage(
        model = mediaAttachment.url,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = if (mediaAttachment.meta?.original != null) {
            Modifier
                .fillMaxWidth()
                .aspectRatio(
                    mediaAttachment.meta.original.aspect.toFloat() ?: 1.5f
                )
        } else {
            Modifier.fillMaxWidth()
        }
    )
}

@Composable
@androidx.annotation.OptIn(UnstableApi::class)
private fun VideoPlayer(
    uri: Uri, mediaAttachment: MediaAttachment, viewModel: PostViewModel
) {
    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    var hasAudio by remember { mutableStateOf(false) }

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
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
            DisposableEffect(key1 = AndroidView(factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                    setShowPreviousButton(false)
                    useController = false
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(
                    mediaAttachment.meta?.original?.aspect?.toFloat() ?: 1.5f
                )
                .onGloballyPositioned { }), effect = {
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

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.isVisible(
    threshold: Int, onVisibilityChange: (Boolean) -> Unit
) = composed {

    Modifier.onGloballyPositioned { layoutCoordinates: LayoutCoordinates ->
        val layoutHeight = layoutCoordinates.size.height
        val thresholdHeight = layoutHeight * threshold / 100
        val layoutTop = layoutCoordinates.positionInRoot().y
        val layoutBottom = layoutTop + layoutHeight

        val parent = layoutCoordinates.parentLayoutCoordinates

        parent?.boundsInRoot()?.let { rect: Rect ->
            val parentTop = rect.top
            val parentBottom = rect.bottom

            if (parentBottom - layoutTop > thresholdHeight && (parentTop < layoutBottom - thresholdHeight)) {
                onVisibilityChange(true)
            } else {
                onVisibilityChange(false)

            }
        }
    }
}