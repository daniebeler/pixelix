package com.daniebeler.pixels.ui.composables

import androidx.compose.runtime.Composable
import com.daniebeler.pixels.MainViewModel

@Composable
fun LocalTimeline(viewModel: MainViewModel) {

    val posts = viewModel.localTimeline


}