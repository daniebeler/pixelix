package com.daniebeler.pixels.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.daniebeler.pixels.MainViewModel

@Composable
fun LocalTimeline(viewModel: MainViewModel) {

    val posts = viewModel.localTimeline

    Column {
        Button(onClick = { viewModel.getLocalTimeline() }) {
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            items(posts) { post ->
                PostComposable(post = post)
            }
        }
    }
}