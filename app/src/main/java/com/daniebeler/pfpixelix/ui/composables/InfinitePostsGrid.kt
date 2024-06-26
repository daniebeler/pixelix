package com.daniebeler.pfpixelix.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pfpixelix.ui.composables.states.FixedHeightEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FixedHeightLoadingComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenLoadingComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfinitePostsGrid(
    items: List<Post>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    error: String,
    endReached: Boolean = false,
    emptyMessage: EmptyState,
    navController: NavController,
    getItemsPaginated: () -> Unit = { },
    before: @Composable (() -> Unit)? = null,
    onRefresh: () -> Unit = { }
) {

    val lazyGridState = rememberLazyGridState()

    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing, onRefresh = { onRefresh() })

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            state = lazyGridState,
            columns = GridCells.Fixed(3)
        ) {

            if (before != null) {
                item(span = { GridItemSpan(3) }) {
                    before()
                }
            }

            items(items, key = {
                it.id
            }) { photo ->
                CustomPost(post = photo, navController = navController)
            }

            if (endReached && items.size > 10) {
                item(span = { GridItemSpan(3) }) {
                    EndOfListComposable()
                }
            }

            if (before != null) {
                if (isLoading) {
                    item(span = { GridItemSpan(3) }) {
                        FixedHeightLoadingComposable()
                    }
                }


                if (items.isEmpty()) {
                    if (!isLoading && error.isEmpty()) {
                        item(span = { GridItemSpan(3) }) {
                            FixedHeightEmptyStateComposable(emptyMessage)
                        }
                    }
                }
            }
        }

        if (items.isEmpty() && error.isNotBlank()) {
            FullscreenErrorComposable(message = error)
        }

        if (before == null && items.isEmpty()) {
            if (isLoading && !isRefreshing) {
                FullscreenLoadingComposable()
            }

            if (!isLoading && error.isEmpty()) {
                FullscreenEmptyStateComposable(emptyMessage)
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
}


