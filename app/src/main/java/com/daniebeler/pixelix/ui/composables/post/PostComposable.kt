package com.daniebeler.pixelix.ui.composables.post

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.model.MediaAttachment
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.model.Reply
import com.daniebeler.pixelix.ui.composables.FollowButton
import com.daniebeler.pixelix.ui.composables.HashtagsMentionsTextView
import com.daniebeler.pixelix.utils.BlurHashDecoder
import com.daniebeler.pixelix.utils.Navigate
import com.daniebeler.pixelix.utils.Share
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PostComposable(
    post: Post,
    navController: NavController,
    viewModel: PostViewModel = hiltViewModel(key = post.id)
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

    val pagerState = rememberPagerState(pageCount = {
        post.mediaAttachments.count()
    })


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
                Text(text = post.account.displayname)
                Text(
                    text = viewModel.timeAgoString + " • @" + post.account.acct,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
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

                Image(
                    blurHashAsDrawable.bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.aspectRatio(
                        post.mediaAttachments[0].meta?.original?.aspect?.toFloat() ?: 1.5f
                    )
                )

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

                Text(text = viewModel.likeState.likesCount.toString() + " likes",
                    modifier = Modifier.clickable {
                        viewModel.loadLikedBy(post.id)
                        showBottomSheet = 3
                    })

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



            if (post.content.isNotBlank()) {
                HashtagsMentionsTextView(
                    text = post.content,
                    mentions = post.mentions,
                    navController = navController
                )
            }

            if (post.replyCount > 0) {
                TextButton(onClick = {
                    viewModel.loadReplies(post.account.id, post.id)
                    showBottomSheet = 1
                }) {
                    Text(text = "View " + post.replyCount + " comments")
                }
            }
        }
    }

    if (showBottomSheet > 0) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = 0
            }, sheetState = sheetState
        ) {
            if (showBottomSheet == 1) {
                CommentsBottomSheet(post, navController, viewModel)
            } else if (showBottomSheet == 2) {
                ShareBottomSheet(context, post.url)
            } else if (showBottomSheet == 3) {
                LikesBottomSheet(viewModel, navController)
            }
        }
    }
}

@Composable
private fun LikesBottomSheet(
    viewModel: PostViewModel, navController: NavController
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text(text = "Liked by", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        HorizontalDivider(Modifier.padding(12.dp))

        LazyColumn {
            items(viewModel.likedByState.likedBy, key = {
                it.id
            }) { account ->
                LikedByAccountElement(account, navController)
            }

            if (viewModel.likedByState.isLoading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .wrapContentSize(Alignment.Center),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            if (!viewModel.likedByState.isLoading && viewModel.likedByState.likedBy.isEmpty()) {
                item {
                    Row(
                        Modifier
                            .padding(vertical = 32.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No likes yet")
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentsBottomSheet(
    post: Post, navController: NavController, viewModel: PostViewModel
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        val ownDescription = Reply("0", post.content, post.mentions, post.account)

        ReplyElement(reply = ownDescription, navController = navController)
        HorizontalDivider(Modifier.padding(12.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            items(viewModel.repliesState.replies, key = {
                it.id
            }) { reply ->
                ReplyElement(reply = reply, navController = navController)
            }
            if (viewModel.repliesState.isLoading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .wrapContentSize(Alignment.Center),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            if (!viewModel.repliesState.isLoading && viewModel.repliesState.replies.isEmpty()) {
                item {
                    Row(
                        Modifier
                            .padding(vertical = 32.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No comments yet")
                    }
                }
            }

        }
    }
}

@Composable
private fun ReplyElement(reply: Reply, navController: NavController) {
    Row {
        AsyncImage(model = reply.account.avatar,
            contentDescription = "",
            modifier = Modifier
                .height(32.dp)
                .clip(CircleShape)
                .clickable {
                    Navigate().navigate(
                        "profile_screen/" + reply.account.id, navController
                    )
                })

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(text = reply.account.acct,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    Navigate().navigate(
                        "profile_screen/" + reply.account.id, navController
                    )
                })

            HashtagsMentionsTextView(
                text = reply.content,
                mentions = reply.mentions,
                navController = navController
            )
        }
    }
}

@Composable
private fun LikedByAccountElement(account: Account, navController: NavController) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                Navigate().navigate("profile_screen/" + account.id, navController)
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = account.avatar,
            contentDescription = "",
            modifier = Modifier
                .height(46.dp)
                .width(46.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = "@${account.username}")
            Text(
                text = "${account.followersCount} followers",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ShareBottomSheet(context: Context, url: String) {
    Column(
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        CustomBottomSheetElement(icon = Icons.Outlined.OpenInBrowser, text = stringResource(
            R.string.open_in_browser
        ), onClick = {
            Navigate().openUrlInApp(context, url)
        })

        CustomBottomSheetElement(icon = Icons.Outlined.Share,
            text = stringResource(R.string.share_this_post),
            onClick = {
                Share().shareText(context, url)
            })
    }
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

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        val blurHashAsDrawable = BlurHashDecoder.blurHashBitmap(
            LocalContext.current.resources,
            mediaAttachment.blurHash,
        )

        Image(
            blurHashAsDrawable.bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(
                mediaAttachment.meta?.original?.aspect?.toFloat() ?: 1.5f
            )
        )

        if (mediaAttachment.type == "image") {
            AsyncImage(
                model = mediaAttachment.url,
                contentDescription = "",
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(
                        mediaAttachment.meta?.original?.aspect?.toFloat() ?: 1.5f
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(onDoubleTap = {
                            if (!viewModel.likeState.isLoading && viewModel.likeState.error == "") {
                                CoroutineScope(Dispatchers.Default).launch {
                                    viewModel.likePost(postId)
                                    showHeart = true
                                }
                            }
                        })
                    },
                contentScale = ContentScale.FillWidth
            )
        } else {
            VideoPlayer(uri = Uri.parse(mediaAttachment.url))
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

    }
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer(uri: Uri) {
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