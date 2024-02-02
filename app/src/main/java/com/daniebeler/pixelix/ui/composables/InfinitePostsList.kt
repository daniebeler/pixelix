package com.daniebeler.pixelix.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.ui.composables.post.PostComposable
import com.daniebeler.pixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pixelix.ui.composables.states.FixedHeightLoadingComposable
import com.daniebeler.pixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pixelix.ui.composables.states.FullscreenLoadingComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfinitePostsList(
    items: List<Post>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    error: String,
    endReached: Boolean,
    emptyMessage: @Composable () -> Unit = { Text(text = "No Posts") },
    navController: NavController,
    getItemsPaginated: () -> Unit,
    onRefresh: () -> Unit,
    itemGetsDeleted: (postId: String) -> Unit
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
            if (items.isNotEmpty()) {
                items(items, key = {
                    it.id
                }) { item ->
                    PostComposable(
                        post = item,
                        postGetsDeleted = ::delete,
                        navController = navController
                    )
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
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                emptyMessage()
            }
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