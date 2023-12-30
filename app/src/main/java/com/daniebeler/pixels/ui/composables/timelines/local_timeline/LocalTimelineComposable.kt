package com.daniebeler.pixels.ui.composables.timelines.local_timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixels.ui.composables.PostComposable

@Composable
fun LocalTimelineComposable(navController: NavController, viewModel: LocalTimelineViewModel = hiltViewModel()) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(viewModel.localTimeline, key = {
            it.id
        }) { item ->
            PostComposable(post = item, navController)
        }
    }
}