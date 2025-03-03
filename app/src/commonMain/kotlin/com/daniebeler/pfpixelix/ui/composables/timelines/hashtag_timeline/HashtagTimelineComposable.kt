package com.daniebeler.pfpixelix.ui.composables.timelines.hashtag_timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.di.injectViewModel
import com.daniebeler.pfpixelix.ui.composables.FollowButton
import com.daniebeler.pfpixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pfpixelix.ui.composables.InfinitePostsGrid
import com.daniebeler.pfpixelix.ui.composables.InfinitePostsList
import com.daniebeler.pfpixelix.ui.composables.ToTopButton
import com.daniebeler.pfpixelix.ui.composables.profile.AccountState
import com.daniebeler.pfpixelix.ui.composables.profile.PostsState
import com.daniebeler.pfpixelix.ui.composables.profile.PostsWrapperComposable
import com.daniebeler.pfpixelix.ui.composables.profile.SwitchViewComposable
import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.utils.Navigate
import com.daniebeler.pfpixelix.utils.StringFormat
import org.jetbrains.compose.resources.stringResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.posts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HashtagTimelineComposable(
    navController: NavController,
    hashtag: String,
    viewModel: HashtagTimelineViewModel = injectViewModel(key = "hashtag-timeline$hashtag") { hashtagTimelineViewModel }
) {

    LaunchedEffect(hashtag) {
        viewModel.getItemsFirstLoad(hashtag)
        viewModel.getHashtagInfo(hashtag)
        viewModel.getRelatedHashtags(hashtag)
    }

    val lazyGridState = rememberLazyListState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(scrollBehavior = scrollBehavior, title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "#$hashtag",
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

            }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ""
                    )
                }
            }, actions = {
                FollowButton(firstLoaded = viewModel.hashtagState.hashtag != null,
                    isLoading = viewModel.hashtagState.isLoading,
                    isFollowing = viewModel.hashtagState.hashtag?.following ?: false,
                    onFollowClick = { viewModel.followHashtag(viewModel.hashtagState.hashtag!!.name) },
                    onUnFollowClick = { viewModel.unfollowHashtag(viewModel.hashtagState.hashtag!!.name) })
            })

        }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            InfinitePostsList(
                items = viewModel.postsState.hashtagTimeline,
                isLoading = viewModel.postsState.isLoading,
                isRefreshing = viewModel.postsState.isRefreshing,
                error = viewModel.postsState.error,
                endReached = viewModel.postsState.endReached,
                view = viewModel.view,
                changeView = { viewModel.changeView(it) },
                isFirstItemLarge = true,
                itemGetsDeleted = { viewModel.postGetsDeleted(it) },
                getItemsPaginated = { viewModel.getItemsPaginated(hashtag) },
                onRefresh = { viewModel.refresh() },
                postsCount = viewModel.hashtagState.hashtag?.count ?: 0,
                navController = navController,
                postGetsUpdated = { viewModel.postGetsUpdated(it) }
            )
        }
    }
}