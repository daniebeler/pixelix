package com.daniebeler.pixelix.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComposable(viewModel: LoginViewModel = hiltViewModel()) {

    val context = LocalContext.current

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
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        viewModel.login("pixelfed.social", context)
                    }
                }) {
                    Text(text = "Pixelfed.social")
                }
                Button(onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        viewModel.login("pixelfed.de", context)
                    }
                }) {
                    Text(text = "Pixelfed.de")
                }
            }

            TextField(
                value = viewModel.customUrl,
                onValueChange = { viewModel.customUrl = it },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                CoroutineScope(Dispatchers.Default).launch {
                    viewModel.login(viewModel.customUrl, context)
                }
            }) {
                Text(text = "login")
            }

            if (viewModel.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}

