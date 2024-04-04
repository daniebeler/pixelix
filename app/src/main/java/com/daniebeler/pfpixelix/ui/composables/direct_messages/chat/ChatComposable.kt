package com.daniebeler.pfpixelix.ui.composables.direct_messages.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatComposable(
    navController: NavController,
    accountId: String,
    viewModel: ChatViewModel = hiltViewModel(key = "chat$accountId")
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.getChat(accountId)
    }

    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            if (viewModel.chatState.chat != null) {
                Row(
                    modifier = Modifier.clickable {
                        Navigate.navigate("profile_screen/$accountId", navController)
                    }, verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = viewModel.chatState.chat!!.avatar,
                        contentDescription = "",
                        modifier = Modifier
                            .height(46.dp)
                            .width(46.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Column {

                        Text(text = viewModel.chatState.chat!!.username ?: "")
                        Text(
                            text = viewModel.chatState.chat!!.url.substringAfter("https://")
                                .substringBefore("/"),
                            fontSize = 12.sp,
                            lineHeight = 6.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ""
                )
            }
        })
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                LazyColumn(state = lazyListState, modifier = Modifier.weight(1f), content = {
                    if (viewModel.chatState.chat != null && viewModel.chatState.chat?.messages!!.isNotEmpty()) {
                        items(viewModel.chatState.chat!!.messages, key = {
                            it.id
                        }) {
                            ConversationElementComposable(
                                message = it, navController = navController
                            )
                        }
                    }

                    if (viewModel.chatState.chat != null && viewModel.chatState.chat?.messages?.isEmpty() == true) {
                        item {
                            Spacer(modifier = Modifier.height(56.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(
                                        RoundedCornerShape(8.dp)
                                    )
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "This is the beginning of your chat with this user. Don't forget to be respectful.",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                })


                Row {
                    TextField(value = viewModel.newMessage,
                        onValueChange = { viewModel.newMessage = it })
                    Button(onClick = { viewModel.sendMessage(accountId) }) {
                        Text(text = "send")
                    }
                }


                ErrorComposable(message = viewModel.chatState.error)
            }
        }

    }
}