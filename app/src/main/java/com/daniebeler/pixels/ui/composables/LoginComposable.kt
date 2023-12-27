package com.daniebeler.pixels.ui.composables

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.daniebeler.pixels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComposable(viewModel: LoginViewModel = hiltViewModel()) {

    val context = LocalContext.current

    var loading: Boolean by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Login")
                }
            )

        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(paddingValues)
        ) {
            Button(onClick = {
                loading = true
                CoroutineScope(Dispatchers.Default).launch {
                    val clientId = viewModel.setBaseUrl(viewModel.customUrl)

                    if (clientId.isNotEmpty()) {
                        openUrl(context, clientId, viewModel.customUrl)
                    }
                }
            }) {
                Text(text = "Pixelfed.social")
            }

            TextField(
                value = viewModel.customUrl,
                onValueChange = { viewModel.customUrl = it },
                modifier = Modifier.fillMaxWidth()
            )

            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}

private fun openUrl(context: Context, clientId: String, baseUrl: String) {
    val intent = CustomTabsIntent.Builder().build()
    val url =
        "${baseUrl}/oauth/authorize?response_type=code&redirect_uri=pixels-android-auth://callback&client_id=" + clientId
    intent.launchUrl(context, Uri.parse(url))
}