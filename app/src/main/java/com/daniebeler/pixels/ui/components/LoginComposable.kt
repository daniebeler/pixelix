package com.daniebeler.pixels.ui.components

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComposable(viewModel: MainViewModel) {

    val context = LocalContext.current

    viewModel.registerApplication()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Login")
                }
            )

        }
    ) {paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(paddingValues)
        ) {
            Button(onClick = {
                if (viewModel._authApplication?.clientId != null) {
                    openUrl(context, viewModel._authApplication?.clientId!!)
                }
            }) {
                Text(text = "Pixelfed.social")
            }
        }
    }
}

fun openUrl(context: Context, clientId: String){
    val intent = CustomTabsIntent.Builder().build()
    val url = "https://pixelfed.social/oauth/authorize?response_type=code&redirect_uri=pixels-android-auth://callback&client_id=" + clientId
    intent.launchUrl(context, Uri.parse(url))
}