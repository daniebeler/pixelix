package com.daniebeler.pixels.ui.composables.timelines

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixels.ui.composables.PostComposable

@Composable
fun HomeTimelineComposable(navController: NavController, viewModel: HomeTimelineViewModel = hiltViewModel()) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(viewModel.homeTimeline, key = {
            it.id
        }) { item ->
            PostComposable(post = item, navController)
        }
    }
}