package com.daniebeler.pixels.ui.composables.settings.followed_hashtags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixels.ui.composables.ErrorComposable
import com.daniebeler.pixels.ui.composables.LoadingComposable
import com.daniebeler.pixels.ui.composables.trending.trending_hashtags.CustomHashtag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowedHashtagsComposable(navController: NavController, viewModel: FollowedHashtagsViewModel = hiltViewModel()) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Followed Hashtags")
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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(viewModel.followedHashtagsState.followedHashtags) { tag ->
                        CustomHashtag(hashtag = tag, navController = navController)
                    }
                })
            
            LoadingComposable(isLoading = viewModel.followedHashtagsState.isLoading)
            ErrorComposable(message = viewModel.followedHashtagsState.error)
        }
        
    }
}