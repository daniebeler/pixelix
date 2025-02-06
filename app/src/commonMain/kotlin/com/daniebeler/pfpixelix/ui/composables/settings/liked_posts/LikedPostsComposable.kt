package com.daniebeler.pfpixelix.ui.composables.settings.liked_posts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.di.injectViewModel
import com.daniebeler.pfpixelix.ui.composables.InfinitePostsGrid
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.chevron_back_outline
import pixelix.app.generated.resources.liked_posts
import pixelix.app.generated.resources.no_liked_posts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikedPostsComposable(
    navController: NavController,
    viewModel: LikedPostsViewModel = injectViewModel(key = "likey-posts-key") { likedPostsViewModel }
) {

    Scaffold(contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(stringResource(Res.string.liked_posts), fontWeight = FontWeight.Bold)
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = vectorResource(Res.drawable.chevron_back_outline), contentDescription = ""
                )
            }
        })

    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            InfinitePostsGrid(items = viewModel.likedPostsState.likedPosts,
                isLoading = viewModel.likedPostsState.isLoading,
                isRefreshing = viewModel.likedPostsState.isRefreshing,
                error = viewModel.likedPostsState.error,
                emptyMessage = EmptyState(
                    icon = Icons.Outlined.FavoriteBorder,
                    heading = stringResource(Res.string.no_liked_posts)
                ),
                navController = navController,
                getItemsPaginated = {
                    viewModel.getItemsPaginated()
                },
                onRefresh = {
                    viewModel.getItemsFirstLoad(true)
                })
        }

    }
}