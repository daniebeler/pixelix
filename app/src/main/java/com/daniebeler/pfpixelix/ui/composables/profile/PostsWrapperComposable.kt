package com.daniebeler.pfpixelix.ui.composables.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.ui.composables.CustomPost
import com.daniebeler.pfpixelix.ui.composables.post.PostComposable
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pfpixelix.ui.composables.states.FixedHeightEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FixedHeightLoadingComposable

fun LazyGridScope.PostsWrapperComposable(
    accountState: AccountState,
    postsState: PostsState,
    navController: NavController,
    emptyState: EmptyState,
    refresh: () -> Unit,
    getPostsPaginated: () -> Unit,
    view: ViewEnum,
    postGetsDeleted: (postId: String) -> Unit
) {

    if (view == ViewEnum.Grid) {
        PostsGridInScope(items = postsState.posts,
            isLoading = postsState.isLoading,
            isRefreshing = accountState.isLoading && accountState.account != null,
            error = postsState.error,
            emptyMessage = emptyState,
            endReached = postsState.endReached,
            navController = navController,
            getItemsPaginated = { getPostsPaginated() },
            before = { }) {
            refresh()
        }
    }

    if (view == ViewEnum.Timeline) {
        PostsListInScope(items = postsState.posts,
            isLoading = postsState.isLoading,
            isRefreshing = accountState.isLoading && accountState.account != null,
            error = postsState.error,
            emptyMessage = emptyState,
            endReached = postsState.endReached,
            navController = navController,
            getItemsPaginated = { getPostsPaginated() },
            onRefresh = { refresh() },
            itemGetsDeleted = {},
            postGetsDeleted = { postGetsDeleted(it) })
    }
}

private fun LazyGridScope.PostsGridInScope(
    items: List<Post>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    error: String,
    endReached: Boolean = false,
    emptyMessage: EmptyState,
    navController: NavController,
    getItemsPaginated: () -> Unit = { },
    before: @Composable (() -> Unit)? = null,
    onRefresh: () -> Unit = { },
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

private fun LazyGridScope.PostsListInScope(
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
    postGetsDeleted: (postId: String) -> Unit
) {

    if (before != null) {
        item {
            before()
        }
    }
    if (items.isNotEmpty()) {
        items(items, span = { GridItemSpan(3) }, key = {
            it.id
        }) { item ->
            val zIndex = remember {
                mutableFloatStateOf(1f)
            }
            Box(modifier = Modifier.zIndex(zIndex.floatValue)) {
                PostComposable(
                    post = item,
                    postGetsDeleted = { postGetsDeleted(it) },
                    navController = navController,
                    setZindex = {
                        zIndex.floatValue = it
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