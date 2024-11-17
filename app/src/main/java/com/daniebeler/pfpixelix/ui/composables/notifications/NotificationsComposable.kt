package com.daniebeler.pfpixelix.ui.composables.notifications

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.daniebeler.pfpixelix.widget.notifications.NotificationWidgetReceiver

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NotificationsComposable(
    navController: NavController,
    viewModel: NotificationsViewModel = hiltViewModel(key = "notifications-viewmodel-key")
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.notificationsState.isRefreshing,
        onRefresh = { viewModel.refresh() })

    val lazyListState = rememberLazyListState()
    val scrollState = rememberScrollState()
    val context: Context = LocalContext.current

    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        CenterAlignedTopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Text(stringResource(R.string.notifications), fontWeight = FontWeight.Bold)
        }, actions = {
            IconButton(onClick = {
                pinWidget(context)
            }) {
                Icon(imageVector = Icons.Outlined.Widgets, contentDescription = "add widget")
            }
        })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                Row(modifier = Modifier.horizontalScroll(scrollState)) {
                    Spacer(modifier = Modifier.width(12.dp))
                    if (viewModel.filter == NotificationsFilterEnum.All) {
                        ActiveFilterButton(text = stringResource(R.string.all))
                    } else {
                        InactiveFilterButton(text = stringResource(R.string.all), onClick = {
                            viewModel.changeFilter(NotificationsFilterEnum.All)
                        })
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    if (viewModel.filter == NotificationsFilterEnum.Followers) {
                        ActiveFilterButton(text = stringResource(id = R.string.followers))
                    } else {
                        InactiveFilterButton(
                            text = stringResource(id = R.string.followers),
                            onClick = {
                                viewModel.changeFilter(NotificationsFilterEnum.Followers)
                            })
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    if (viewModel.filter == NotificationsFilterEnum.Likes) {
                        ActiveFilterButton(text = stringResource(R.string.likes_))
                    } else {
                        InactiveFilterButton(text = stringResource(R.string.likes_), onClick = {
                            viewModel.changeFilter(NotificationsFilterEnum.Likes)
                        })
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    if (viewModel.filter == NotificationsFilterEnum.Reposts) {
                        ActiveFilterButton(text = stringResource(R.string.reposts))
                    } else {
                        InactiveFilterButton(text = stringResource(R.string.reposts), onClick = {
                            viewModel.changeFilter(NotificationsFilterEnum.Reposts)
                        })
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState),
                    content = {
                        if (viewModel.notificationsState.notifications.isNotEmpty()) {
                            items(viewModel.notificationsState.notifications, key = {
                                it.id
                            }) {
                                if (viewModel.filter == NotificationsFilterEnum.All) {
                                    CustomNotification(
                                        notification = it, navController = navController
                                    )
                                } else if (viewModel.filter == NotificationsFilterEnum.Likes && it.type == "favourite") {
                                    CustomNotification(
                                        notification = it, navController = navController
                                    )
                                } else if (viewModel.filter == NotificationsFilterEnum.Followers && it.type == "follow") {
                                    CustomNotification(
                                        notification = it, navController = navController
                                    )
                                } else if (viewModel.filter == NotificationsFilterEnum.Reposts && it.type == "reblog") {
                                    CustomNotification(
                                        notification = it, navController = navController
                                    )
                                }
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
            }


            if (!viewModel.notificationsState.isLoading && viewModel.notificationsState.error.isEmpty() && viewModel.notificationsState.notifications.isEmpty()) {
                FullscreenEmptyStateComposable(
                    EmptyState(
                        icon = Icons.Outlined.Email, heading = stringResource(
                            R.string.you_don_t_have_any_notifications
                        )
                    )
                )
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
            viewModel.getNotificationsPaginated()
        }
    }
}

private fun pinWidget(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val myProvider = ComponentName(context, NotificationWidgetReceiver::class.java)

    if (appWidgetManager.isRequestPinAppWidgetSupported) {
        appWidgetManager.requestPinAppWidget(myProvider, null, null)
    }
}

@Composable
private fun ActiveFilterButton(text: String) {
    Button(onClick = { }) {
        Text(text = text)
    }
}

@Composable
private fun InactiveFilterButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() }, colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(text = text)
    }
}