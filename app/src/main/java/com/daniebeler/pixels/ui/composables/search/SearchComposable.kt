package com.daniebeler.pixels.ui.composables.search

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixels.ui.composables.CustomAccount
import com.daniebeler.pixels.ui.composables.CustomHashtag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComposable(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Box(Modifier.padding(12.dp, 0.dp)) {
                OutlinedTextField(
                    value = viewModel.textInput,
                    onValueChange = { viewModel.textInputChange(it) },
                    label = { Text("Search") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.background
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            LazyColumn(content = {
                if (viewModel.searchState.searchResult != null) {
                    item {
                        Text(text = "Accounts")
                    }
                    items(viewModel.searchState.searchResult!!.accounts) {
                        CustomAccount(
                            account = it,
                            relationship = null,
                            navController = navController
                        )
                    }
                    item {
                        Text(text = "Hashtags")
                    }
                    items(viewModel.searchState.searchResult!!.tags) {
                        CustomHashtag(hashtag = it, navController = navController)
                    }
                }
            })
        }
    }
}