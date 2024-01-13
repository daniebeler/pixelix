package com.daniebeler.pixelix.ui.composables.timelines.hashtag_timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.InfinitePostsList
import com.daniebeler.pixelix.ui.composables.LoadingComposable

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

        Box(modifier = Modifier.padding(paddingValues)) {
            InfinitePostsList(
                items = viewModel.postsState.hashtagTimeline,
                isLoading = viewModel.postsState.isLoading,
                isRefreshing = viewModel.postsState.isRefreshing,
                navController = navController,
                getItemsPaginated = {
                    viewModel.getItemsPaginated(hashtag)
                }
            )
        }

        CustomPullRefreshIndicator(
            viewModel.postsState.isRefreshing,
            pullRefreshState,
        )

        LoadingComposable(isLoading = viewModel.postsState.isLoading && viewModel.postsState.hashtagTimeline.isEmpty())

        ErrorComposable(message = viewModel.postsState.error, pullRefreshState)
    }
}