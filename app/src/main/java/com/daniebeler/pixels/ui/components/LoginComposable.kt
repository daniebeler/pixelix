package com.daniebeler.pixels.ui.components

import android.accounts.AccountManager
import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComposable(viewModel: MainViewModel, navController: NavController) {


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

            }) {
                Text(text = "Pixelfed.social")
            }
        }
    }
}