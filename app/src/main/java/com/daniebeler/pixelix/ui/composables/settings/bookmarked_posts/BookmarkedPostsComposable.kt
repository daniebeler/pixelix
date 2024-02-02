package com.daniebeler.pixelix.ui.composables.settings.bookmarked_posts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.InfinitePostsGrid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BookmarkedPostsComposable(
    navController: NavController,
    viewModel: BookmarkedPostsViewModel = hiltViewModel()
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.bookmarkedPostsState.isLoading,
        onRefresh = { viewModel.getBookmarkedPosts() }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Text(stringResource(id = R.string.bookmarked_posts))
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
                }
            )

        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            InfinitePostsGrid(
                items = viewModel.bookmarkedPostsState.bookmarkedPosts,
                isLoading = viewModel.bookmarkedPostsState.isLoading,
                isRefreshing = viewModel.bookmarkedPostsState.isRefreshing,
                error = viewModel.bookmarkedPostsState.error,
                endReached = false,
                emptyMessage = {
                    Text(text = stringResource(R.string.no_bookmarked_posts))
                },
                navController = navController,
                getItemsPaginated = { /*TODO*/ },
                onRefresh = { viewModel.getBookmarkedPosts(true) })
        }

    }
}