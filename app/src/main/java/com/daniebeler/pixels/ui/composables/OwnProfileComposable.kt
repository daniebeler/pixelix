package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnProfileComposable(viewModel: MainViewModel, navController: NavController) {

    var account = viewModel.ownAccount

    val posts = viewModel.ownPosts

    CoroutineScope(Dispatchers.Default).launch {
        if (posts.isEmpty()) {
            viewModel.getOwnPosts()
        }
    }

    fun loadMorePosts() {
        if (posts.isNotEmpty()) {
            val maxId = posts.last().id

            CoroutineScope(Dispatchers.Default).launch {
                viewModel.getMoreOwnPosts(maxId)
            }
        }
    }


    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = account?.username ?: "")
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
        if (account != null) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(paddingValues),
                    content = {
                        if (account != null) {
                            item (
                                span = { GridItemSpan(3) }
                            ) {
                                ProfileTopSection(account = account)
                            }
                        }

                        items(posts, key = {
                            it.id
                        }) { photo ->
                            CustomPost(post = photo, navController = navController)
                        }

                        item {
                            Button(onClick = {
                                loadMorePosts()
                            }) {
                                Text(text = "Load More")
                            }
                        }

                    }
                )
        }

    }
}