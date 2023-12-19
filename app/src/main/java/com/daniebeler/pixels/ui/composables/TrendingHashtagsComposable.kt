package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel
import com.daniebeler.pixels.api.models.Hashtag

@Composable
fun TrendingHashtagsComposable(viewModel: MainViewModel, navController: NavController) {

    val trendingHashtags = viewModel.trendingHashtags

    LazyColumn(content = {
        items(trendingHashtags) {
            CustomHashtag(hashtag = it, navController = navController)
        }
    })
}

@Composable
fun CustomHashtag(hashtag: Hashtag, navController: NavController) {
   Text(text = hashtag.name)
}