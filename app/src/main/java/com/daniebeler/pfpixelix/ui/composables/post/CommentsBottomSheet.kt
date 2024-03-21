package com.daniebeler.pfpixelix.ui.composables.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.model.Reply
import com.daniebeler.pfpixelix.ui.composables.hashtagMentionText.HashtagsMentionsTextView
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FixedHeightLoadingComposable
import com.daniebeler.pfpixelix.utils.Navigate
import com.daniebeler.pfpixelix.utils.TimeAgo

@Composable
fun CommentsBottomSheet(
    post: Post, navController: NavController, viewModel: PostViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .imePadding()
                .fillMaxHeight()
                .align(Alignment.TopStart)
        ) {
            item {
                if (post.content.isNotEmpty()) {
                    val ownDescription =
                        Reply(
                            "0",
                            post.content,
                            post.mentions,
                            post.account,
                            post.createdAt,
                            post.replyCount,
                            post.likedBy
                        )
                    ReplyElement(
                        reply = ownDescription,
                        true,
                        navController = navController,
                        {},
                        viewModel.myAccountId
                    )
                }

                CreateComment ({ replyText -> viewModel.createReply(post.id, replyText) }, post.id, viewModel.ownReplyState)

                HorizontalDivider(Modifier.padding(12.dp))
            }

            items(viewModel.repliesState.replies, key = {
                it.id
            }) { reply ->
                ReplyElement(reply = reply, false, navController = navController,
                    { viewModel.deleteReply(reply.id) }, viewModel.myAccountId
                )
            }

            if (viewModel.repliesState.isLoading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }

            if (!viewModel.repliesState.isLoading && viewModel.repliesState.replies.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 32.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(R.string.no_comments_yet))
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Spacer(
                    Modifier
                        .windowInsetsBottomHeight(WindowInsets.navigationBars)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                )
            }
        }

    }
}


@Composable
private fun ReplyElement(
    reply: Reply,
    postDescription: Boolean,
    navController: NavController,
    deleteReply: () -> Unit,
    myAccountId: String?,
    viewModel: ReplyElementViewModel = hiltViewModel(key = reply.id)
) {

    var timeAgo: String by remember { mutableStateOf("") }
    var replyCount: Int by remember { mutableIntStateOf(reply.replyCount) }
    val openAddReplyDialog = remember { mutableStateOf(false) }

    LaunchedEffect(reply.createdAt) {
        if (myAccountId != null) {
            viewModel.onInit(reply, myAccountId)
        }
        timeAgo = TimeAgo.convertTimeToText(reply.createdAt)
    }
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row {
            AsyncImage(model = reply.account.avatar,
                contentDescription = "",
                modifier = Modifier
                    .height(42.dp)
                    .width(42.dp)
                    .clip(CircleShape)
                    .clickable {
                        Navigate.navigate(
                            "profile_screen/" + reply.account.id, navController
                        )
                    })

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row {
                    Text(text = reply.account.acct,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            Navigate.navigate(
                                "profile_screen/" + reply.account.id, navController
                            )
                        })

                    Text(
                        text = " • $timeAgo",
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }


                HashtagsMentionsTextView(
                    text = reply.content, mentions = reply.mentions, navController = navController
                )
            }
        }

        if (!postDescription) {
            Row (Modifier.padding(54.dp, 0.dp, 0.dp, 0.dp)) {
                if (reply.account.id == myAccountId) {
                    TextButton(onClick = { deleteReply() }) {
                        Text(text = "Delete", color = MaterialTheme.colorScheme.onBackground)
                    }
                }
                TextButton(onClick = { openAddReplyDialog.value = true }) {
                    Text(text = "Reply", color = MaterialTheme.colorScheme.onBackground)
                }
                if (viewModel.likedReply) {
                    TextButton(onClick = { viewModel.unlikeReply(reply.id) }) {
                        Text(text = "Liked")
                    }
                } else {
                    TextButton(onClick = { viewModel.likeReply(reply.id) }) {
                        Text(text = "Like", color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }

            if (replyCount != 0 && viewModel.repliesState.replies.isEmpty()) {
                Box(modifier = Modifier.padding(54.dp, 0.dp, 0.dp, 0.dp)) {
                    TextButton(onClick = { viewModel.loadReplies(reply.account.id, reply.id) }) {
                        Text(
                            text = if (replyCount == 1) {
                                "view $replyCount reply"
                            } else {
                                "view $replyCount replies"
                            }, fontSize = 12.sp
                        )
                    }
                }
            }
            if (viewModel.repliesState.isLoading) {
                Box(modifier = Modifier.padding(54.dp, 0.dp, 0.dp, 0.dp)) {
                    FixedHeightLoadingComposable()
                }
            } else if (viewModel.repliesState.error != "") {
                Box(modifier = Modifier.padding(54.dp, 0.dp, 0.dp, 0.dp)) {
                    ErrorComposable(viewModel.repliesState.error)
                }
            } else if (viewModel.repliesState.replies.isNotEmpty()) {
                Box(Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp)) {
                    Column {
                        viewModel.repliesState.replies.map {
                            ReplyElement(reply = it, false, navController = navController,
                                {
                                    viewModel.deleteReply(it.id)
                                    replyCount--
                                }, myAccountId
                            )
                        }
                    }
                }
            }
        }
    }
    if (openAddReplyDialog.value) {
        AddReplyDialog(onDismissRequest = { openAddReplyDialog.value = false }, onConfirmation = {
            openAddReplyDialog.value = false
            if (myAccountId != null) {
                viewModel.createReply(reply.id, it, myAccountId)
            }
        })
    }
}

@Composable
fun AddReplyDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (replyText: String) -> Unit,
) {
    var replyText by remember { mutableStateOf("") }

    AlertDialog(
        icon = {
            Icon(Icons.Outlined.Edit, contentDescription = "Edit")
        },
        title = {
            Text(text = "Add Reply")
        },
        text = {
            TextField(value = replyText, onValueChange = { replyText = it })
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(replyText)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}