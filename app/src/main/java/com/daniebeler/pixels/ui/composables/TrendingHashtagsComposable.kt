package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixels.domain.model.Tag

@Composable
fun TrendingHashtagsComposable(navController: NavController, viewModel: TrendingHashtagsViewModel = hiltViewModel()) {

    LazyColumn(content = {
        items(viewModel.trendingHashtags, key = {
            it.name
        }) {
            CustomHashtag(hashtag = it, navController = navController)
        }
    })
}

@Composable
fun CustomHashtag(hashtag: Tag, navController: NavController) {
   Text(text = hashtag.name,
       Modifier
           .clickable {
               val newHastag = hashtag.name.drop(1)
               navController.navigate("hashtag_timeline_screen/$newHastag") {
                   launchSingleTop = true
                   restoreState = true
               }
           }
           .padding(start = 6.dp, top = 6.dp)
   )
}