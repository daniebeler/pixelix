package com.daniebeler.pixelix.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.ui.composables.post.PostComposable

@Composable
fun InfinitePostsList(items: List<Post>, isLoading: Boolean, isRefreshing: Boolean, navController: NavController, getItemsPaginated: () -> Unit) {

    val lazyListState = rememberLazyListState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        //modifier = Modifier.pullRefresh(pullRefreshState),
        state = lazyListState
    ) {
        items(items, key = {
            it.id
        }) { item ->
            PostComposable(post = item, navController)
        }

        if (items.isNotEmpty() && isLoading && isRefreshing) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }

    InfiniteListHandler(lazyListState = lazyListState) {
        getItemsPaginated()
    }
}