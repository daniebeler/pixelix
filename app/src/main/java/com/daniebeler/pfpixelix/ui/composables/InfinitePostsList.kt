package com.daniebeler.pfpixelix.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.ui.composables.post.PostComposable
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pfpixelix.ui.composables.states.FixedHeightLoadingComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenLoadingComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfinitePostsList(
    items: List<Post>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    error: String,
    endReached: Boolean,
    emptyMessage: EmptyState,
    navController: NavController,
    getItemsPaginated: () -> Unit,
    onRefresh: () -> Unit,
    itemGetsDeleted: (postId: String) -> Unit,
    before: @Composable (() -> Unit)? = null,
) {

    val lazyListState = rememberLazyListState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { onRefresh() }
    )

    fun delete(postId: String) {
        itemGetsDeleted(postId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            state = lazyListState
        ) {
            if (before != null) {
                item {
                    before()
                }
            }

            if (items.isNotEmpty()) {

                items(items, key = {
                    it.id
                }) { item ->
                    val zIndex = remember {
                        mutableFloatStateOf(1f)
                    }
                    Box(modifier = Modifier.zIndex(zIndex.floatValue)) {
                        PostComposable(
                            post = item,
                            postGetsDeleted = ::delete,
                            navController = navController,
                            setZindex = { zIndex.floatValue = it
                            }
                        )
                    }

                }

                if (isLoading && !isRefreshing) {
                    item {
                        FixedHeightLoadingComposable()
                    }
                }

                if (endReached && items.size > 3) {
                    item {
                        EndOfListComposable()
                    }
                }
            }
        }

        if (items.isEmpty() && !isLoading && error.isEmpty()) {
            FullscreenEmptyStateComposable(emptyMessage)
        }

        if (!isRefreshing && items.isEmpty() && isLoading) {
            FullscreenLoadingComposable()
        }

        if (error.isNotEmpty() && items.isEmpty()) {
            FullscreenErrorComposable(message = error)
        }
    }

    InfiniteListHandler(lazyListState = lazyListState) {
        getItemsPaginated()
    }

    CustomPullRefreshIndicator(
        isRefreshing,
        pullRefreshState,
    )
}