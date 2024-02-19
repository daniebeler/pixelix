package com.daniebeler.pfpixelix.ui.composables.states

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorComposable(message: String) {

    if (message.isNotBlank()) {
        InnerErrorComposable(message = message)
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
            InnerErrorComposable(message = message)
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
            InnerErrorComposable(message = message)
        }
    }
}

@Composable
private fun InnerErrorComposable(message: String) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Outlined.WarningAmber,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Text(text = "Error", fontSize = 38.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message, Modifier.wrapContentSize(
                Alignment.Center
            ), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface
        )
    }
}