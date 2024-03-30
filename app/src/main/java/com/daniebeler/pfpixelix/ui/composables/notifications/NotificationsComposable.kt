package com.daniebeler.pfpixelix.ui.composables.notifications

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pfpixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.LoadingComposable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NotificationsComposable(
    navController: NavController, viewModel: NotificationsViewModel = hiltViewModel()
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.notificationsState.isRefreshing,
        onRefresh = { viewModel.refresh() })

    val lazyListState = rememberLazyListState()

    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Text(stringResource(R.string.notifications))
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
                    if (viewModel.notificationsState.notifications.isNotEmpty()) {
                        items(viewModel.notificationsState.notifications, key = {
                            it.id
                        }) {
                            CustomNotification(notification = it, navController = navController)
                        }

                        if (viewModel.notificationsState.isLoading && !viewModel.notificationsState.isRefreshing) {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .wrapContentSize(Alignment.Center)
                                )
                            }
                        }

                        if (viewModel.notificationsState.endReached && viewModel.notificationsState.notifications.size > 10) {
                            item {
                                EndOfListComposable()
                            }
                        }
                    }
                })

            if (!viewModel.notificationsState.isLoading && viewModel.notificationsState.error.isEmpty() && viewModel.notificationsState.notifications.isEmpty()) {
                FullscreenEmptyStateComposable(EmptyState(icon = Icons.Outlined.Email, heading = stringResource(
                    R.string.you_don_t_have_any_notifications
                )
                ))
            }

            CustomPullRefreshIndicator(
                viewModel.notificationsState.isRefreshing, pullRefreshState
            )

            if (!viewModel.notificationsState.isRefreshing && viewModel.notificationsState.notifications.isEmpty()) {
                LoadingComposable(isLoading = viewModel.notificationsState.isLoading)
            }
            ErrorComposable(message = viewModel.notificationsState.error, pullRefreshState)
        }

        InfiniteListHandler(lazyListState = lazyListState) {
            println("fieef")
            viewModel.getNotificationsPaginated()
        }
    }
}