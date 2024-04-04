package com.daniebeler.pfpixelix.ui.composables.direct_messages.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pfpixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pfpixelix.ui.composables.direct_messages.conversations.ConversationsViewModel
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.LoadingComposable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ChatComposable(
    navController: NavController, accountId: String, viewModel: ChatViewModel = hiltViewModel()
) {
    val lazyListState = rememberLazyListState()

    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Text("Chat")

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
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(state = lazyListState,
                modifier = Modifier
                    .fillMaxSize(),
                content = {
                    if (viewModel.chatState.conversations.isNotEmpty()) {
                        items(viewModel.chatState.conversations, key = {
                            it.id
                        }) {
                            Box(modifier = Modifier.clickable {  }) {
                                //CustomNotification(notification = it, navController = navController)
                                Text(text = it.id.toString())
                            }
                        }

                        if (viewModel.chatState.isLoading && !viewModel.chatState.isRefreshing) {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .wrapContentSize(Alignment.Center)
                                )
                            }
                        }

                        if (viewModel.chatState.endReached && viewModel.chatState.conversations.size > 10) {
                            item {
                                EndOfListComposable()
                            }
                        }
                    }
                })

            if (!viewModel.chatState.isLoading && viewModel.chatState.error.isEmpty() && viewModel.chatState.conversations.isEmpty()) {
                FullscreenEmptyStateComposable(
                    EmptyState(
                        icon = Icons.Outlined.Email, heading = stringResource(
                            R.string.you_don_t_have_any_notifications
                        )
                    )
                )
            }

            if (!viewModel.chatState.isRefreshing && viewModel.chatState.conversations.isEmpty()) {
                LoadingComposable(isLoading = viewModel.chatState.isLoading)
            }
            ErrorComposable(message = viewModel.chatState.error)
        }

        InfiniteListHandler(lazyListState = lazyListState) {
            //viewModel.getNotificationsPaginated()
        }
    }
}