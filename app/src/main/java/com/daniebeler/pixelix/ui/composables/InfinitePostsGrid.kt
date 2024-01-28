package com.daniebeler.pixelix.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixelix.domain.model.Post

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfinitePostsGrid(
    items: List<Post>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    error: String,
    endReached: Boolean,
    emptyMessage: @Composable () -> Unit,
    navController: NavController,
    getItemsPaginated: () -> Unit,
    before: @Composable () -> Unit,
    onRefresh: () -> Unit
) {

    val lazyGridState = rememberLazyGridState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { onRefresh() }
    )

    LazyVerticalGrid(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.pullRefresh(pullRefreshState),
        state = lazyGridState,
        columns = GridCells.Fixed(3)
    ) {
        item(span = { GridItemSpan(3) }) {
            before()
        }

        items(items, key = {
            it.id
        }) { photo ->
            CustomPost(post = photo, navController = navController)
        }

        if (items.isNotEmpty() && isLoading) {
            item(span = { GridItemSpan(3) }) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .wrapContentSize(Alignment.Center)
                )
            }
        }

        if (items.isEmpty() && !isLoading && error.isEmpty()) {
            item(span = { GridItemSpan(3) }) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(36.dp, 20.dp)
                ) {
                    emptyMessage()
                }
            }
        }

        if (endReached && items.size > 10) {
            item(span = { GridItemSpan(3) }) {
                EndOfListComposable()
            }
        }
    }

    InfiniteGridHandler(lazyGridState = lazyGridState) {
        getItemsPaginated()
    }

    CustomPullRefreshIndicator(
        isRefreshing,
        pullRefreshState,
    )

    if (!isRefreshing && items.isEmpty()) {
        LoadingComposable(isLoading = isLoading)
    }
    ErrorComposable(message = error, pullRefreshState)
}