package com.daniebeler.pfpixelix.ui.composables.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.ui.composables.CustomPost
import com.daniebeler.pfpixelix.ui.composables.post.PostComposable
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pfpixelix.ui.composables.states.FixedHeightEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FixedHeightLoadingComposable

fun LazyListScope.PostsWrapperComposable(
    accountState: AccountState,
    postsState: PostsState,
    navController: NavController,
    emptyState: EmptyState,
    view: ViewEnum,
    postGetsDeleted: (postId: String) -> Unit,
    isFirstImageLarge: Boolean = false,
    screenWidth: Dp? = null
) {

    if (view == ViewEnum.Grid) {
        PostsGridInScope(
            items = postsState.posts,
            isLoading = postsState.isLoading,
            error = postsState.error,
            emptyMessage = emptyState,
            endReached = postsState.endReached,
            navController = navController,
            before = { },
            isFirstImageLarge = isFirstImageLarge,
            screenWidth = screenWidth
        )
    }

    if (view == ViewEnum.Timeline) {
        PostsListInScope(items = postsState.posts,
            isLoading = postsState.isLoading,
            isRefreshing = accountState.isLoading && accountState.account != null,
            endReached = postsState.endReached,
            navController = navController,
            postGetsDeleted = { postGetsDeleted(it) })
    }
}

private fun LazyListScope.PostsGridInScope(
    items: List<Post>,
    isLoading: Boolean,
    error: String,
    endReached: Boolean = false,
    emptyMessage: EmptyState,
    navController: NavController,
    before: @Composable (() -> Unit)? = null,
    isFirstImageLarge: Boolean = false,
    screenWidth: Dp?
) {

    if (before != null) {
        item {
            before()
        }
    }

    if (isFirstImageLarge && items.size >= 3 && screenWidth != null) {
        var largePostRoundedCorners = Modifier.clip(RoundedCornerShape(topStart = 16.dp))
        var thirdPostRoundedCorners: Modifier = Modifier
        if (items.size == 3) {
            largePostRoundedCorners =
                largePostRoundedCorners.clip(RoundedCornerShape(bottomStart = 16.dp))
            thirdPostRoundedCorners =
                thirdPostRoundedCorners.clip(RoundedCornerShape(bottomEnd = 16.dp))

        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(horizontal = 12.dp)

            ) {
                Box(modifier = Modifier.width((screenWidth - 24.dp) / 3 * 2 - 1.dp)) {
                    CustomPost(
                        post = items.first(),
                        navController = navController,
                        isFullQuality = true,
                        customModifier = largePostRoundedCorners
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    CustomPost(
                        post = items[1],
                        navController = navController,
                        customModifier = Modifier.clip(
                            RoundedCornerShape(
                                topEnd = 16.dp
                            )
                        )
                    )
                    CustomPost(
                        post = items[2],
                        navController = navController,
                        customModifier = thirdPostRoundedCorners
                    )

                }
            }
        }


        val rows = items.takeLast(items.size - 3).chunked(3)
        itemsIndexed(rows) {rowIndex, rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Fill the row with 3 items (or fewer for the last row)
                rowItems.forEachIndexed { index, post ->

                    var roundedCorners: Modifier = Modifier
                    val isBottomLeft = rowItems == rows.last() && index == 0
                    val isBottomRight = (rowItems == rows.last() && (index == 2 || index == rowItems.size-1)) || (index == 2 && items.size - (rowIndex+2)*3 < 3)

                    if (isBottomLeft) {
                        roundedCorners =
                            roundedCorners.clip(RoundedCornerShape(bottomStart = 16.dp))
                    }
                    if (isBottomRight) {
                        roundedCorners = roundedCorners.clip(RoundedCornerShape(bottomEnd = 16.dp))
                    }

                    Box(
                        Modifier
                            .padding(horizontal = 2.dp)
                            .weight(1f)
                    ) {

                        CustomPost(
                            post = post, navController = navController, customModifier = roundedCorners
                        )
                    }
                }

                repeat(3 - rowItems.size) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                    )
                }
            }
        }


    } else {
        val rows = items.chunked(3)
        items(rows) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                rowItems.forEachIndexed { index, post ->

                    val isTopLeft = rowItems == rows.first() && index == 0
                    val isTopRight = rowItems == rows.first() && index == 2 || index == rowItems.size-1
                    val isBottomLeft = rowItems == rows.last() && index == 0
                    val isBottomRight = rowItems == rows.last() && index == 2 || index == rowItems.size-1

                    var roundedCorners: Modifier = Modifier

                    if (isTopLeft) {
                        roundedCorners = roundedCorners.clip(RoundedCornerShape(topStart = 16.dp))
                    }
                    if (isBottomLeft) {
                        roundedCorners = roundedCorners.clip(RoundedCornerShape(bottomStart = 16.dp))
                    }
                    if (isTopRight) {
                        roundedCorners = roundedCorners.clip(RoundedCornerShape(topEnd = 16.dp))
                    }
                    if (isBottomRight) {
                        roundedCorners = roundedCorners.clip(RoundedCornerShape(bottomEnd = 16.dp))
                    }

                    Box(
                        Modifier
                            .padding(horizontal = 2.dp)
                            .weight(1f)
                    ) {

                        CustomPost(
                            post = post, navController = navController, customModifier = roundedCorners
                        )
                    }
                }

                // Add empty Composables for spacing if the last row has fewer than 3 items
                repeat(3 - rowItems.size) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                    )
                }
            }
        }
    }


    if (endReached && items.size > 10) {
        item {
            EndOfListComposable()
        }
    }

    if (before != null) {
        if (isLoading) {
            item {
                FixedHeightLoadingComposable()
            }
        }


        if (items.isEmpty()) {
            if (!isLoading && error.isEmpty()) {
                item {
                    FixedHeightEmptyStateComposable(emptyMessage)
                }
            }
        }
    }
}

private fun LazyListScope.PostsListInScope(
    items: List<Post>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    endReached: Boolean,
    navController: NavController,
    before: @Composable (() -> Unit)? = null,
    postGetsDeleted: (postId: String) -> Unit
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
                PostComposable(post = item,
                    postGetsDeleted = { postGetsDeleted(it) },
                    navController = navController,
                    setZindex = {
                        zIndex.floatValue = it
                    })
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