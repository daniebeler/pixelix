package com.daniebeler.pixelix.ui.composables.settings.liked_posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.CustomPost
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.LoadingComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikedPostsComposable(
    navController: NavController,
    viewModel: LikedPostsViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.liked_posts))
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
            if (viewModel.likedPostsState.likedPosts.isEmpty() && !viewModel.likedPostsState.isLoading && viewModel.likedPostsState.error.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "no liked posts",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        items(viewModel.likedPostsState.likedPosts, key = {
                            it.id
                        }) { photo ->
                            CustomPost(post = photo, navController = navController)
                        }
                    })
            }

            LoadingComposable(isLoading = viewModel.likedPostsState.isLoading)
            ErrorComposable(message = viewModel.likedPostsState.error)
        }

    }
}