package com.daniebeler.pixelix.ui.composables.post

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.model.Reply
import com.daniebeler.pixelix.ui.composables.hashtagMentionText.HashtagsMentionsTextView
import com.daniebeler.pixelix.utils.Navigate
import com.daniebeler.pixelix.utils.TimeAgo

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CommentsBottomSheet(
    post: Post, sheetState: SheetState, navController: NavController, viewModel: PostViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        // LazyColumn with replies
        LazyColumn(
            modifier = Modifier
                .imePadding()
                .fillMaxHeight()
                .align(Alignment.TopStart)
        ) {
            item {
                if (post.content.isNotEmpty()) {
                    val ownDescription =
                        Reply("0", post.content, post.mentions, post.account, post.createdAt)
                    ReplyElement(reply = ownDescription, navController = navController)
                }

                Row(verticalAlignment = Alignment.Bottom) {
                    OutlinedTextField(value = viewModel.newComment,
                        onValueChange = { viewModel.newComment = it },
                        label = { Text(stringResource(R.string.reply)) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp, 0.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            viewModel.createReply(post.id)
                        })
                    )

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if (!viewModel.ownReplyState.isLoading) {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                viewModel.createReply(post.id)
                            }
                        },
                        Modifier
                            .height(56.dp)
                            .width(56.dp)
                            .padding(0.dp, 0.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(12.dp),
                        enabled = viewModel.newComment.isNotBlank()
                    ) {
                        if (viewModel.ownReplyState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "submit",
                                Modifier
                                    .fillMaxSize()
                                    .fillMaxWidth()
                            )
                        }

                    }
                }

                HorizontalDivider(Modifier.padding(12.dp))
            }

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
private fun ReplyElement(reply: Reply, navController: NavController) {

    var timeAgo: String by remember { mutableStateOf("") }

    LaunchedEffect(reply.createdAt) {
        timeAgo = TimeAgo().covertTimeToText(reply.createdAt)
    }

    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        AsyncImage(model = reply.account.avatar,
            contentDescription = "",
            modifier = Modifier
                .height(42.dp)
                .width(42.dp)
                .clip(CircleShape)
                .clickable {
                    Navigate().navigate(
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
                        Navigate().navigate(
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
}