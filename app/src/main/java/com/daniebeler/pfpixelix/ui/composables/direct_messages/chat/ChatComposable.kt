package com.daniebeler.pfpixelix.ui.composables.direct_messages.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pfpixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.utils.Navigate
import com.daniebeler.pfpixelix.utils.imeAwareInsets

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ChatComposable(
    navController: NavController,
    accountId: String,
    viewModel: ChatViewModel = hiltViewModel(key = "chat$accountId")
) {
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current
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

        Box(
            modifier = Modifier
                .imeAwareInsets(context, 100.dp)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                LazyColumn(state = lazyListState,
                    modifier = Modifier.weight(1f),
                    reverseLayout = true,
                    content = {
                        if (viewModel.chatState.chat != null && viewModel.chatState.chat?.messages!!.isNotEmpty()) {

                            items(viewModel.chatState.chat!!.messages, key = {
                                it.id
                            }) {
                                ConversationElementComposable(
                                    message = it, { viewModel.deleteMessage(it.reportId) }, navController = navController
                                )
                            }

                            if (viewModel.chatState.isLoading) {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp)
                                            .wrapContentSize(Alignment.Center)
                                    )
                                }
                            }

                            if (viewModel.chatState.endReached) {
                                item {
                                    EndOfListComposable()
                                }
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
                                        text = stringResource(R.string.beginning_of_chat_note),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    })


                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                    OutlinedTextField(value = viewModel.newMessage,
                        onValueChange = { viewModel.newMessage = it },
                        label = { Text("Message") },
                        singleLine = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedBorderColor = MaterialTheme.colorScheme.background
                        ),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(12.dp))
                    if (viewModel.newMessageState.isLoading) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .height(56.dp)
                                .width(56.dp)
                                .padding(0.dp, 0.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                viewModel.sendMessage(accountId)
                            },
                            Modifier
                                .height(56.dp)
                                .width(56.dp)
                                .padding(0.dp, 0.dp),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "send",
                                Modifier
                                    .fillMaxSize()
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                ErrorComposable(message = viewModel.chatState.error)
            }
        }
        InfiniteListHandler(lazyListState = lazyListState) {
            viewModel.getChatPaginated(accountId)
        }
    }
}