package com.daniebeler.pixels.ui.composables.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixels.ui.composables.followers.CustomFollowerElement

@Composable
fun SearchComposable(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    Scaffold {paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(
                value = viewModel.textInput,
                onValueChange = { viewModel.textInputChange(it) },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(content = {
                if (viewModel.searchState.searchResult != null) {
                    items(viewModel.searchState.searchResult!!.accounts) {
                        CustomFollowerElement(account = it, relationship = null, navController = navController)
                    }
                }
            })
        }
    }
}