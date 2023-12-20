package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeTimelineComposable(viewModel: MainViewModel, navController: NavController) {

    val items = viewModel.homeTimeline

    CoroutineScope(Dispatchers.Default).launch {
        viewModel.gotDataFromDataStore.collect { state ->
            if (state) {
                if (items.isEmpty()) {
                    viewModel.getHomeTimeline()
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