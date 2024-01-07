package com.daniebeler.pixelix.ui.composables.settings.followed_hashtags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.CustomHashtag
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.LoadingComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowedHashtagsComposable(
    navController: NavController,
    viewModel: FollowedHashtagsViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.followed_hashtags))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )

        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (viewModel.followedHashtagsState.followedHashtags.isEmpty() && !viewModel.followedHashtagsState.isLoading && viewModel.followedHashtagsState.error.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "no followed hashtags",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        items(viewModel.followedHashtagsState.followedHashtags) { tag ->
                            CustomHashtag(hashtag = tag, navController = navController)
                        }
                    })
            }
            LoadingComposable(isLoading = viewModel.followedHashtagsState.isLoading)
            ErrorComposable(message = viewModel.followedHashtagsState.error)
        }

    }
}