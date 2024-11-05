package com.daniebeler.pfpixelix.ui.composables.settings.followed_hashtags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.CustomHashtag
import com.daniebeler.pfpixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenLoadingComposable
import com.daniebeler.pfpixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun FollowedHashtagsComposable(
    navController: NavController,
    viewModel: FollowedHashtagsViewModel = hiltViewModel(key = "followed-hashtags-key")
) {
    val pullRefreshState =
        rememberPullRefreshState(refreshing = viewModel.followedHashtagsState.isRefreshing,
            onRefresh = { viewModel.getFollowedHashtags(true) })
    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        CenterAlignedTopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Text(stringResource(id = R.string.followed_hashtags), fontWeight = FontWeight.Bold)
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
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState),
                content = {
                    items(viewModel.followedHashtagsState.followedHashtags) { tag ->
                        CustomHashtag(hashtag = tag, navController = navController)
                    }
                })

            if (viewModel.followedHashtagsState.followedHashtags.isEmpty()) {
                if (viewModel.followedHashtagsState.isLoading && !viewModel.followedHashtagsState.isRefreshing) {
                    FullscreenLoadingComposable()
                }

                if (viewModel.followedHashtagsState.error.isNotEmpty()) {
                    FullscreenErrorComposable(message = viewModel.followedHashtagsState.error)
                }

                if (!viewModel.followedHashtagsState.isLoading && viewModel.followedHashtagsState.error.isEmpty()) {
                    FullscreenEmptyStateComposable(
                        EmptyState(icon = Icons.Outlined.Tag,
                            heading = stringResource(R.string.no_followed_hashtags),
                            message = "Followed hashtags will appear here",
                            buttonText = "Explore trending hashtags",
                            onClick = {
                                Navigate.navigate("trending_screen/hashtags", navController)
                            })
                    )
                }
            }

            CustomPullRefreshIndicator(
                viewModel.followedHashtagsState.isRefreshing,
                pullRefreshState,
            )
        }

    }
}