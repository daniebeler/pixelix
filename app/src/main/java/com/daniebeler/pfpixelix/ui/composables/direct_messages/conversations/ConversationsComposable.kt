package com.daniebeler.pfpixelix.ui.composables.direct_messages.conversations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pfpixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pfpixelix.ui.composables.SheetItem
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.LoadingComposable
import com.daniebeler.pfpixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ConversationsComposable(
    navController: NavController, viewModel: ConversationsViewModel = hiltViewModel()
) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showNewChatDialog = remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.conversationsState.isRefreshing,
        onRefresh = { viewModel.refresh() })

    val lazyListState = rememberLazyListState()

    Scaffold(contentWindowInsets = WindowInsets(0.dp), floatingActionButton = {
        FloatingActionButton(onClick = {
            showNewChatDialog.value = true
        }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }

    }, topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Text("Messages")

        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ""
                )
            }
        }, actions = {
            IconButton(onClick = { showBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Outlined.ReportProblem,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )
            }
        })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState),
                content = {
                    if (viewModel.conversationsState.conversations.isNotEmpty()) {
                        items(viewModel.conversationsState.conversations, key = {
                            it.id
                        }) {
                            ConversationElementComposable(
                                conversation = it, navController = navController
                            )
                        }

                        if (viewModel.conversationsState.isLoading && !viewModel.conversationsState.isRefreshing) {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .wrapContentSize(Alignment.Center)
                                )
                            }
                        }

                        if (viewModel.conversationsState.endReached && viewModel.conversationsState.conversations.size > 10) {
                            item {
                                EndOfListComposable()
                            }
                        }
                    }
                })

            if (!viewModel.conversationsState.isLoading && viewModel.conversationsState.error.isEmpty() && viewModel.conversationsState.conversations.isEmpty()) {
                FullscreenEmptyStateComposable(
                    EmptyState(
                        icon = Icons.Outlined.Email, heading = stringResource(
                            R.string.you_don_t_have_any_notifications
                        )
                    )
                )
            }

            CustomPullRefreshIndicator(
                viewModel.conversationsState.isRefreshing, pullRefreshState
            )

            if (!viewModel.conversationsState.isRefreshing && viewModel.conversationsState.conversations.isEmpty()) {
                LoadingComposable(isLoading = viewModel.conversationsState.isLoading)
            }
            ErrorComposable(message = viewModel.conversationsState.error, pullRefreshState)
        }

        InfiniteListHandler(lazyListState = lazyListState) {
            //viewModel.getNotificationsPaginated()
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                windowInsets = WindowInsets.navigationBars, onDismissRequest = {
                    showBottomSheet = false
                }, sheetState = sheetState
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(18.dp))

                        SheetItem(
                            header = stringResource(R.string.warning),
                            description = stringResource(R.string.direct_messages_encryption_description)
                        )
                    }
                }
            }
        }
        if (showNewChatDialog.value) {
            CreateNewConversation(
                close = {
                    showNewChatDialog.value = false
                    viewModel.newConversationUsername = TextFieldValue()
                    viewModel.newConversationSelectedAccount = null
                    viewModel.newConversationState = NewConversationState()
                }, viewModel, navController
            )
        }
    }
}

@Composable
private fun CreateNewConversation(
    close: () -> Unit, viewModel: ConversationsViewModel, navController: NavController
) {

    AlertDialog(title = {
        Text(text = stringResource(R.string.new_direct_message))
    }, text = {
        Column {
            OutlinedTextField(
                value = viewModel.newConversationUsername,
                onValueChange = {
                    viewModel.changeNewConversationUsername(it)
                },
                label = { Text(stringResource(R.string.select_recipient)) },
                shape = RoundedCornerShape(12.dp),
            )
            if (viewModel.newConversationState.suggestions.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        viewModel.newConversationState.suggestions.map {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    viewModel.newConversationUsername =
                                        TextFieldValue(
                                            it.acct,
                                            selection = TextRange(it.acct.length)
                                        )
                                    viewModel.newConversationSelectedAccount = it
                                    viewModel.newConversationState = NewConversationState()
                                }) {
                                Text(
                                    text = "@${it.acct}",
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    }, onDismissRequest = {
        close()
    }, confirmButton = {
        TextButton(enabled = viewModel.newConversationSelectedAccount != null, onClick = {
            if (viewModel.newConversationSelectedAccount != null) {
                Navigate.navigate(
                    "chat/" + viewModel.newConversationSelectedAccount!!.id, navController
                )
                close()
            }
        }) {
            Text(stringResource(R.string.confirm))
        }
    }, dismissButton = {
        TextButton(onClick = {
            close()
        }) {
            Text(stringResource(R.string.cancel))
        }
    })

}