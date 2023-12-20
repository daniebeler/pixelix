package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel
import com.daniebeler.pixels.api.models.Hashtag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TrendingHashtagsComposable(viewModel: MainViewModel, navController: NavController) {

    val trendingHashtags = viewModel.trendingHashtags

    CoroutineScope(Dispatchers.Default).launch {
        viewModel.gotDataFromDataStore.collect { state ->
            if (state) {
                if (trendingHashtags.isEmpty()) {
                    viewModel.getTrendingHashtags()
                }
            }
        }
    }

    LazyColumn(content = {
        items(trendingHashtags) {
            CustomHashtag(hashtag = it, navController = navController)
        }
    })
}

@Composable
fun CustomHashtag(hashtag: Hashtag, navController: NavController) {
   Text(text = hashtag.name, Modifier.clickable {
       navController.navigate("hashtag_timeline_screen/" + hashtag.name) {
           launchSingleTop = true
           restoreState = true
       }
   })
}