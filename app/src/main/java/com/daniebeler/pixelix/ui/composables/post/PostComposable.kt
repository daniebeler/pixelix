package com.daniebeler.pixelix.ui.composables.post

import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.domain.model.MediaAttachment
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.ui.composables.states.LoadingComposable
import com.daniebeler.pixelix.ui.composables.hashtagMentionText.HashtagsMentionsTextView
import com.daniebeler.pixelix.utils.BlurHashDecoder
import com.daniebeler.pixelix.utils.Navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PostComposable(
    post: Post,
    navController: NavController,
    postGetsDeleted: (postId: String) -> Unit,
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
        viewModel.likeState = LikeState(liked = post.favourited, likesCount = post.favouritesCount)
        viewModel.bookmarkState = BookmarkState(bookmarked = post.bookmarked)
    }

    LaunchedEffect(viewModel.deleteState.deleted) {
        if (viewModel.deleteState.deleted) {
            postGetsDeleted(post.id)
        }
    }

    val mediaAttachmentsCount = post.mediaAttachments.count()

    val pagerState = rememberPagerState(pageCount = { mediaAttachmentsCount })

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable(onClick = {
                    Navigate().navigate("profile_screen/" + post.account.id, navController)
                })
        ) {
            AsyncImage(
                model = post.account.avatar,
                contentDescription = "",
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = post.account.displayname ?: "")
                Text(
                    text = viewModel.timeAgoString + " • @" + post.account.acct,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (post.place != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "",
                            modifier = Modifier.height(20.dp)
                        )
                        Row {
                            Text(text = post.place.name ?: "", fontSize = 12.sp)
                            if (post.place.country != null) {
                                Text(text = ", " + (post.place.country ?: ""), fontSize = 12.sp)
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
                    imageVector = Icons.Outlined.MoreVert, contentDescription = ""
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (post.sensitive && !viewModel.showPost) {

            Box {
                val blurHashAsDrawable = BlurHashDecoder.blurHashBitmap(
                    LocalContext.current.resources, post.mediaAttachments[0].blurHash
                )

                if (blurHashAsDrawable.bitmap != null) {
                    Image(
                        blurHashAsDrawable.bitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.aspectRatio(
                            post.mediaAttachments[0].meta?.original?.aspect?.toFloat() ?: 1.5f
                        )
                    )
                }


                Column(
                    Modifier.aspectRatio(
                        post.mediaAttachments[0].meta?.original?.aspect?.toFloat() ?: 1.5f
                    ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (post.spoilerText.isNotEmpty()) {
                        Text(text = post.spoilerText)
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
            if (post.mediaAttachments.count() > 1) {
                HorizontalPager(state = pagerState, beyondBoundsPageCount = 1) { page ->
                    PostImage(
                        mediaAttachment = post.mediaAttachments[page], post.id, viewModel
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp), horizontalArrangement = Arrangement.Center
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
            } else {
                PostImage(
                    mediaAttachment = post.mediaAttachments[0], post.id, viewModel
                )
            }
        }

        Column(Modifier.padding(8.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row {
                    if (viewModel.likeState.liked) {
                        IconButton(onClick = {
                            viewModel.unlikePost(post.id)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            viewModel.likePost(post.id)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder, contentDescription = ""
                            )
                        }
                    }

                    IconButton(onClick = {
                        viewModel.loadReplies(post.account.id, post.id)
                        showBottomSheet = 1
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = ""
                        )
                    }
                }

                Row {
                    Spacer(modifier = Modifier.width(40.dp))

                    if (viewModel.bookmarkState.bookmarked) {
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
                                imageVector = Icons.Outlined.BookmarkBorder, contentDescription = ""
                            )
                        }
                    }
                }
            }

            Row {
                if (post.likedBy?.username?.isNotBlank() == true) {
                    Text(text = stringResource(id = R.string.liked_by) + " ", fontSize = 14.sp)
                    Text(
                        text = post.likedBy.username,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            Navigate().navigate("profile_screen/" + post.likedBy.id, navController)
                        })
                    if (post.likedBy.others) {
                        Text(text = " " + stringResource(id = R.string.and) + " ", fontSize = 14.sp)
                        Text(text = post.likedBy.totalCount.toString() + " " + stringResource(id = R.string.others),
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

            if (post.content.isNotBlank()) {
                HashtagsMentionsTextView(
                    text = post.content, mentions = post.mentions, navController = navController
                )
            }

            if (post.replyCount > 0) {
                TextButton(onClick = {
                    viewModel.loadReplies(post.account.id, post.id)
                    showBottomSheet = 1
                }) {
                    Text(text = stringResource(R.string.view_comments, post.replyCount))
                }
            }
        }
    }

    if (showBottomSheet > 0) {
        ModalBottomSheet(
            windowInsets = WindowInsets.navigationBars, onDismissRequest = {
                showBottomSheet = 0
            }, sheetState = sheetState
        ) {
            if (showBottomSheet == 1) {
                CommentsBottomSheet(post, sheetState, navController, viewModel)
            } else if (showBottomSheet == 2) {
                if (viewModel.myAccountId != null && post.account.id == viewModel.myAccountId) {
                    ShareBottomSheet(context, post.url, true, viewModel, post)
                } else {
                    ShareBottomSheet(context, post.url, false, viewModel, post)
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

@Composable
fun CustomBottomSheetElement(icon: ImageVector, text: String, onClick: () -> Unit) {

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            Modifier.padding(start = 18.dp, top = 12.dp, bottom = 12.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(text = text)
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun PostImage(
    mediaAttachment: MediaAttachment, postId: String, viewModel: PostViewModel
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
        modifier = Modifier.fillMaxWidth()
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


        Box(modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onDoubleTap = {
                if (!viewModel.likeState.isLoading && viewModel.likeState.error == "") {
                    CoroutineScope(Dispatchers.Default).launch {
                        viewModel.likePost(postId)
                        showHeart = true
                    }
                }
            })
        }) {
            if (mediaAttachment.type == "image" && mediaAttachment.url?.takeLast(4) != ".gif") {
                ImageWrapper(mediaAttachment)
            } else if (mediaAttachment.url?.takeLast(4) == ".gif") {
                GifPlayer(mediaAttachment)
            } else {
                VideoPlayer(uri = Uri.parse(mediaAttachment.url))
            }
        }

        if (mediaAttachment.description?.isNotBlank() == true) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                onClick = {
                    altText = mediaAttachment.description
                },
                colors = IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Outlined.Description, contentDescription = "Show alt text")
            }
        }

        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.Center)
                .scale(scale.value)
        )

        if (altText.isNotBlank()) {
            AlertDialog(
                title = {
                    Text(text = stringResource(R.string.media_description))
                },
                text = {
                    Text(text = altText)
                },
                onDismissRequest = {
                    altText = ""
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            altText = ""
                        }
                    ) {
                        Text(stringResource(id = android.R.string.ok))
                    }
                }
            )
        }

    }
}

@Composable
private fun ImageWrapper(mediaAttachment: MediaAttachment) {
    AsyncImage(
        model = mediaAttachment.url,
        contentDescription = "",
        Modifier
            .fillMaxWidth()
            .aspectRatio(
                mediaAttachment.meta?.original?.aspect?.toFloat() ?: 1f
            ),
        contentScale = ContentScale.FillWidth
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun GifPlayer(mediaAttachment: MediaAttachment) {
    GlideImage(
        model = mediaAttachment.url,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(
                mediaAttachment.meta?.original?.aspect?.toFloat() ?: 1.5f
            )
    )
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
private fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val defaultDataSourceFactory = DefaultDataSource.Factory(context)
            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                context, defaultDataSourceFactory
            )
            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))

            setMediaSource(source)
            prepare()
        }
    }

    exoPlayer.playWhenReady = true
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                hideController()
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        })
    ) {
        onDispose { exoPlayer.release() }
    }
}