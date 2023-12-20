package com.daniebeler.pixels.ui.composables.timelines

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel
import com.daniebeler.pixels.ui.composables.PostComposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LocalTimelineComposable(viewModel: MainViewModel, navController: NavController) {

    val items = viewModel.localTimeline

    CoroutineScope(Dispatchers.Default).launch {
        viewModel.gotDataFromDataStore.collect { state ->
            if (state) {
                if (items.isEmpty()) {
                    viewModel.getLocalTimeline()
                }
            }
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(items) { item ->
            PostComposable(post = item, navController)
        }
    }
}