package com.daniebeler.pfpixelix.ui.composables.states

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorComposable(message: String) {

    if (message.isNotBlank()) {
        Text(
            text = message,
            Modifier
                .fillMaxSize()
                .wrapContentSize(
                    Alignment.Center
                )
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorComposable(message: String, refreshState: PullRefreshState) {
    if (message.isNotBlank()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(refreshState)
                .verticalScroll(rememberScrollState())
                .padding(36.dp, 20.dp)
        ) {
            Text(
                text = message,
                Modifier
                    .fillMaxSize()
                    .wrapContentSize(
                        Alignment.Center
                    )
            )
        }
    }
}


@Composable
fun FullscreenErrorComposable(message: String) {
    if (message.isNotBlank()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp, 20.dp)
        ) {
            Text(
                text = message,
                Modifier
                    .fillMaxSize()
                    .wrapContentSize(
                        Alignment.Center
                    )
            )
        }
    }
}