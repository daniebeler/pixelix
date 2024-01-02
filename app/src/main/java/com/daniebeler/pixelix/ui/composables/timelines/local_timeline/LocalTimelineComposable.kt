package com.daniebeler.pixelix.ui.composables.timelines.local_timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.LoadingComposable
import com.daniebeler.pixelix.ui.composables.PostComposable

@Composable
fun LocalTimelineComposable(navController: NavController, viewModel: LocalTimelineViewModel = hiltViewModel()) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(viewModel.localTimelineState.localTimeline, key = {
            it.id
        }) { item ->
            PostComposable(post = item, navController)
        }

        if (viewModel.localTimelineState.localTimeline.isNotEmpty()) {
            item {
                Button(onClick = {
                    viewModel.loadMorePosts()
                }) {
                    Text(text = stringResource(R.string.load_more))
                }
            }
        }
    }

    LoadingComposable(isLoading = viewModel.localTimelineState.isLoading)
    ErrorComposable(message = viewModel.localTimelineState.error)
}