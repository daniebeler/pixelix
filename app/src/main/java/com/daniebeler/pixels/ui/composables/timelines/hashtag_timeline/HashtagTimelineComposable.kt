package com.daniebeler.pixels.ui.composables.timelines.hashtag_timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixels.ui.composables.ErrorComposable
import com.daniebeler.pixels.ui.composables.LoadingComposable
import com.daniebeler.pixels.ui.composables.PostComposable
import com.daniebeler.pixels.ui.composables.timelines.hashtag_timeline.HashtagTimelineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HashtagTimelineComposable(navController: NavController, hashtag: String, viewModel: HashtagTimelineViewModel = hiltViewModel()) {

    viewModel.getHashtagTimeline(hashtag)
    viewModel.getHashtagInfo(hashtag)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Column {
                        Text("#$hashtag", lineHeight = 10.sp)
                        if (viewModel.hashtagState.hashtag != null) {
                            Text(text = viewModel.hashtagState.hashtag!!.count.toString() + " posts", fontSize = 14.sp, lineHeight = 12.sp, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    
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
                },
                actions = {
                    if (viewModel.hashtagState.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    } else if (viewModel.hashtagState.hashtag != null) {
                        if (viewModel.hashtagState.hashtag!!.following) {
                            Button(onClick = {
                                viewModel.unfollowHashtag(viewModel.hashtagState.hashtag!!.name)
                            }) {
                                Text(text = "unfollow")
                            }
                        }
                        else {
                            Button(onClick = {
                                viewModel.followHashtag(viewModel.hashtagState.hashtag!!.name)
                            }) {
                                Text(text = "follow")
                            }
                        }
                    }
                }
            )

        }
    ) {paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(paddingValues)
        ) {
            items(viewModel.postsState.hashtagTimeline, key = {
                it.id
            }) { item ->
                PostComposable(post = item, navController)
            }
        }
        
        LoadingComposable(isLoading = viewModel.postsState.isLoading)
        ErrorComposable(message = viewModel.postsState.error)
    }
}