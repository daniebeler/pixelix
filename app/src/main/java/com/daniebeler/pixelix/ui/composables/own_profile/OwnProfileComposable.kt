package com.daniebeler.pixelix.ui.composables.own_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.LoadingComposable
import com.daniebeler.pixelix.ui.composables.profile.ProfileTopSection
import com.daniebeler.pixelix.ui.composables.trending.trending_posts.CustomPost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnProfileComposable(navController: NavController, viewModel: OwnProfileViewModel = hiltViewModel()) {

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = viewModel.accountState.account?.username ?: "")
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("settings_screen") {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = ""
                        )
                    }
                }
            )

        }
    ) {paddingValues ->
        Box {
            if (viewModel.accountState.account != null) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(paddingValues),
                    content = {
                        if (viewModel.accountState.account != null) {
                            item (
                                span = { GridItemSpan(3) }
                            ) {
                                ProfileTopSection(account = viewModel.accountState.account!!, navController)
                            }
                        }

                        items(viewModel.postsState.posts, key = {
                            it.id
                        }) { photo ->
                            CustomPost(post = photo, navController = navController)
                        }

                        item {
                            Button(onClick = {
                                //viewModel.loadMorePosts()
                            }) {
                                Text(text = stringResource(R.string.load_more))
                            }
                        }

                    }
                )
            }
            
            LoadingComposable(isLoading = viewModel.accountState.isLoading)
            ErrorComposable(message = viewModel.accountState.error)
        }
        

    }
}