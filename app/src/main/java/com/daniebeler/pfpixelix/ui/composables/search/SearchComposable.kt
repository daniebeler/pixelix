package com.daniebeler.pfpixelix.ui.composables.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.SavedSearchItem
import com.daniebeler.pfpixelix.domain.model.SavedSearchType
import com.daniebeler.pfpixelix.ui.composables.CustomHashtag
import com.daniebeler.pfpixelix.ui.composables.custom_account.CustomAccount
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenLoadingComposable
import com.daniebeler.pfpixelix.utils.Navigate

@Composable
fun SearchComposable(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(key = "search-viewmodel-key")
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Scaffold(contentWindowInsets = WindowInsets(0.dp)) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Box(Modifier.padding(12.dp, 0.dp)) {
                OutlinedTextField(
                    value = viewModel.textInput,
                    onValueChange = { viewModel.textInputChange(it) },
                    label = { Text(stringResource(R.string.search)) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        Box(modifier = Modifier.clickable {
                            viewModel.textInput = ""
                            viewModel.searchState = SearchState()
                        }) { Icon(Icons.Filled.Clear, contentDescription = null) }
                    },
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
                    keyboardActions = KeyboardActions(onSearch = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        viewModel.onSearch(viewModel.textInput)
                        viewModel.saveSearch(viewModel.textInput)
                    })
                )
            }
            if (viewModel.textInput.isBlank() && viewModel.savedSearches.pastSearches.isNotEmpty()) {
                LazyColumn {
                    items(viewModel.savedSearches.pastSearches.reversed()) {
                        if (it.savedSearchType == SavedSearchType.Account) {
                            Row {
                                CustomAccount(account = it.account!!,
                                    relationship = null,
                                    navController = navController,
                                    { viewModel.deleteSavedSearch(it) })
                            }
                        } else {
                            PastSearchItem(item = it,
                                navController,
                                { text -> viewModel.onSearch(text) },
                                { viewModel.deleteSavedSearch(it) })
                        }
                    }
                }
            }
            LazyColumn(content = {
                if (viewModel.searchState.searchResult != null) {
                    items(viewModel.searchState.searchResult!!.accounts) {
                        CustomAccount(
                            account = it,
                            relationship = null,
                            onClick = { viewModel.saveAccount(it.username, it) },
                            navController = navController
                        )
                    }
                    item { HorizontalDivider(Modifier.padding(12.dp)) }
                    items(viewModel.searchState.searchResult!!.tags) {
                        CustomHashtag(
                            hashtag = it,
                            onClick = { viewModel.saveHashtag(it.name) },
                            navController = navController
                        )
                    }
                }
            })

            if (viewModel.searchState.isLoading) {
                FullscreenLoadingComposable()
            }
        }
    }
}

@Composable
private fun PastSearchItem(
    item: SavedSearchItem,
    navController: NavController,
    setSearchText: (text: String) -> Unit,
    deleteSavedSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                when (item.savedSearchType) {
                    SavedSearchType.Account -> Navigate.navigate(
                        "profile_screen/" + item.account!!.id, navController
                    )

                    SavedSearchType.Hashtag -> Navigate.navigate(
                        "hashtag_timeline_screen/${item.value}", navController
                    )

                    SavedSearchType.Search -> setSearchText(item.value)
                }
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.savedSearchType == SavedSearchType.Account) {
            AsyncImage(
                model = item.account!!.avatar,
                contentDescription = "",
                modifier = Modifier
                    .height(46.dp)
                    .width(46.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .height(46.dp)
                    .width(46.dp)
                    .background(MaterialTheme.colorScheme.surfaceBright, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.savedSearchType == SavedSearchType.Hashtag) {
                        Icons.Outlined.Tag
                    } else {
                        Icons.Outlined.Search
                    }, contentDescription = null, tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))

        val text = when (item.savedSearchType) {
            SavedSearchType.Account -> {
                "@${item.value}"
            }

            SavedSearchType.Hashtag -> {
                "#${item.value}"
            }

            else -> {
                item.value
            }
        }
        Text(text = text)
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(22.dp)
                .width(22.dp)
                .clickable { deleteSavedSearch() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}