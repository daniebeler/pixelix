package com.daniebeler.pixelix.ui.composables.timelines.hashtag_timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.EndOfListComposable
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pixelix.ui.composables.LoadingComposable
import com.daniebeler.pixelix.ui.composables.post.PostComposable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HashtagTimelineComposable(
    navController: NavController,
    hashtag: String,
    viewModel: HashtagTimelineViewModel = hiltViewModel(key = hashtag)
) {

    LaunchedEffect(hashtag) {
        viewModel.getItemsFirstLoad(hashtag)
        viewModel.getHashtagInfo(hashtag)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.postsState.isRefreshing,
        onRefresh = { viewModel.refresh() }
    )

    val lazyListState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Column {
                        Text("#$hashtag", lineHeight = 10.sp)
                        if (viewModel.hashtagState.hashtag != null) {
                            Text(
                                text = viewModel.hashtagState.hashtag!!.count.toString() + " posts",
                                fontSize = 14.sp,
                                lineHeight = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    if (viewModel.hashtagState.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    } else if (viewModel.hashtagState.hashtag != null) {
                        if (viewModel.hashtagState.hashtag!!.following) {
                            Button(onClick = {
                                viewModel.unfollowHashtag(viewModel.hashtagState.hashtag!!.name)
                            }) {
                                Text(text = "unfollow")
                            }
                        } else {
                            Button(onClick = {
                                viewModel.followHashtag(viewModel.hashtagState.hashtag!!.name)
                            }) {
                                Text(text = "follow")
                            }
                        }
                    }
                }
            )

        }
    ) { paddingValues ->
        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            items(viewModel.postsState.hashtagTimeline, key = {
                it.id
            }) { item ->
                PostComposable(post = item, navController)
            }

            if (viewModel.postsState.hashtagTimeline.isNotEmpty() && viewModel.postsState.isLoading && !viewModel.postsState.isRefreshing) {
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

            if (viewModel.postsState.endReached && viewModel.postsState.hashtagTimeline.size > 2) {
                item {
                    EndOfListComposable()
                }
            }
        }
        CustomPullRefreshIndicator(
            viewModel.postsState.isRefreshing,
            pullRefreshState,
        )

        InfiniteListHandler(lazyListState = lazyListState) {
            viewModel.getItemsPaginated(hashtag)
        }

        LoadingComposable(isLoading = viewModel.postsState.isLoading && viewModel.postsState.hashtagTimeline.isEmpty())

        ErrorComposable(message = viewModel.postsState.error, pullRefreshState)
    }
}