package com.daniebeler.pixelix.ui.composables.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.domain.model.Notification
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.EndOfListComposable
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pixelix.ui.composables.LoadingComposable
import com.daniebeler.pixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NotificationsComposable(
    navController: NavController,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.notificationsState.isRefreshing,
        onRefresh = { viewModel.refresh() }
    )

    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.notifications))
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (viewModel.notificationsState.notifications.isNotEmpty()) {
                Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
                    LazyColumn(
                        state = lazyListState,
                        content = {
                            items(viewModel.notificationsState.notifications, key = {
                                it.id
                            }) {
                                CustomNotificaiton(notification = it, navController = navController)
                            }

                            if (viewModel.notificationsState.notifications.isNotEmpty() && viewModel.notificationsState.isLoading && !viewModel.notificationsState.isRefreshing) {
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

                            if (viewModel.notificationsState.endReached && viewModel.notificationsState.notifications.size > 10) {
                                item {
                                    EndOfListComposable()
                                }
                            }
                        })

                    InfiniteListHandler(lazyListState = lazyListState) {
                        viewModel.getNotificationsPaginated()
                    }
                }
            } else if (!viewModel.notificationsState.isLoading && viewModel.notificationsState.error.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState)
                        .verticalScroll(rememberScrollState())
                        .padding(36.dp, 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.empty_state_no_notifications),
                        contentDescription = null,
                        Modifier.fillMaxWidth()
                    )
                }
            }
            CustomPullRefreshIndicator(
                viewModel.notificationsState.isRefreshing,
                pullRefreshState
            )

            if (!viewModel.notificationsState.isRefreshing && viewModel.notificationsState.notifications.isEmpty()) {
                LoadingComposable(isLoading = viewModel.notificationsState.isLoading)
            }
            ErrorComposable(message = viewModel.notificationsState.error, pullRefreshState)
        }

    }


}

@Composable
fun CustomNotificaiton(notification: Notification, navController: NavController) {

    var showImage = false
    var text = ""
    if (notification.type == "follow") {
        text = " " + stringResource(R.string.followed_you)
    } else if (notification.type == "favourite") {
        text = " " + stringResource(R.string.liked_your_post)
        showImage = true
    } else if (notification.type == "reblog") {
        text = " " + stringResource(R.string.reblogged_your_post)
        showImage = true
    }

    Row(
        Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = notification.account.avatar, contentDescription = "",
            modifier = Modifier
                .height(46.dp)
                .width(46.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    Navigate().navigate("profile_screen/" + notification.account.id, navController)
                }
            ) {
                Text(text = notification.account.username, fontWeight = FontWeight.Bold)

                Text(text = text, overflow = TextOverflow.Ellipsis)
            }


            Text(
                text = notification.timeAgo,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (showImage) {
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = notification.post?.mediaAttachments?.get(0)?.previewUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(36.dp)
                    .aspectRatio(1f)
            )
        }

    }

}