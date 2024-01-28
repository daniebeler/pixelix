package com.daniebeler.pixelix.ui.composables.timelines.hashtag_timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.FollowButton
import com.daniebeler.pixelix.ui.composables.InfinitePostsList

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                scrollBehavior = scrollBehavior,
                title = {
                    Column {
                        Text(
                            "#$hashtag", lineHeight = 10.sp, overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
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
                    FollowButton(
                        firstLoaded = viewModel.hashtagState.hashtag != null,
                        isLoading = viewModel.hashtagState.isLoading,
                        isFollowing = viewModel.hashtagState.hashtag?.following ?: false,
                        onFollowClick = { viewModel.followHashtag(viewModel.hashtagState.hashtag!!.name) },
                        onUnFollowClick = { viewModel.unfollowHashtag(viewModel.hashtagState.hashtag!!.name) })
                }
            )

        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            InfinitePostsList(
                items = viewModel.postsState.hashtagTimeline,
                isLoading = viewModel.postsState.isLoading,
                isRefreshing = viewModel.postsState.isRefreshing,
                error = viewModel.postsState.error,
                endReached = viewModel.postsState.endReached,
                navController = navController,
                getItemsPaginated = {
                    viewModel.getItemsPaginated(hashtag)
                },
                onRefresh = {
                    viewModel.refresh()
                },
                itemGetsDeleted = { postId -> viewModel.postGetsDeleted(postId) }
            )
        }
    }
}