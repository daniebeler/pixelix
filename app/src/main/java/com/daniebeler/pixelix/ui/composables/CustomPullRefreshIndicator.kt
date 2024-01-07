package com.daniebeler.pixelix.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomPullRefreshIndicator(refreshing: Boolean, pullRefreshState: PullRefreshState) {
    Box (Modifier.fillMaxSize()){
        PullRefreshIndicator(
            refreshing,
            pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}