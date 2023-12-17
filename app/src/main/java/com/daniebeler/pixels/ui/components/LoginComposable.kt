package com.daniebeler.pixels.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel
import com.daniebeler.pixels.models.api.Application
import com.daniebeler.pixels.models.api.CountryRepository
import com.daniebeler.pixels.models.api.CountryRepositoryImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComposable(viewModel: MainViewModel, navController: NavController) {

    val repository: CountryRepository = CountryRepositoryImpl()

    val uriHandler = LocalUriHandler.current

    var account: Application by remember {
        mutableStateOf(Application("null", "null", "null", "null", "null"))
    }

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
                    uriHandler.openUri("https://pixelfed.social/oauth/authorize?response_type=code&redirect_uri=pixels-android-auth://callback&client_id=" + viewModel._authApplication?.clientId)
                }
            }) {
                Text(text = "Pixelfed.social")
            }
        }
    }
}