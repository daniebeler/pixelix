package com.daniebeler.pixels.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daniebeler.pixels.MainViewModel

@Composable
fun TestComposable(viewModel: MainViewModel) {

    val items = viewModel.dailyTrendingPosts



            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                items(items) { item ->
                    PostComposable(post = item)
                }
            }

}