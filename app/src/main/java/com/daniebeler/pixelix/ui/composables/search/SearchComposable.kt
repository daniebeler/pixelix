package com.daniebeler.pixelix.ui.composables.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.CustomAccount
import com.daniebeler.pixelix.ui.composables.CustomHashtag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComposable(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
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
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            viewModel.textInputChange(viewModel.textInput)
                        }
                    )

                )
            }
            LazyColumn(content = {
                if (viewModel.searchState.searchResult != null) {
                    items(viewModel.searchState.searchResult!!.accounts) {
                        CustomAccount(
                            account = it,
                            relationship = null,
                            navController = navController
                        )
                    }
                    item { HorizontalDivider(Modifier.padding(12.dp)) }
                    items(viewModel.searchState.searchResult!!.tags) {
                        CustomHashtag(hashtag = it, navController = navController)
                    }
                }
            })
        }
    }
}