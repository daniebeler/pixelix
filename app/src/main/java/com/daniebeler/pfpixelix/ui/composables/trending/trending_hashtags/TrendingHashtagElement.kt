package com.daniebeler.pfpixelix.ui.composables.trending.trending_hashtags

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.Tag
import com.daniebeler.pfpixelix.ui.composables.CustomPost
import com.daniebeler.pfpixelix.utils.Navigate

@Composable
fun TrendingHashtagElement(
    hashtag: Tag,
    navController: NavController,
    viewModel: TrendingHashtagElementViewModel = hiltViewModel(key = "the" + hashtag.name)
) {

    LaunchedEffect(hashtag) {
        viewModel.loadItems(hashtag.name)
        viewModel.getHashtagInfo(hashtag.name)
    }

    Column(
        Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                Navigate.navigate("hashtag_timeline_screen/${hashtag.name}", navController)
            }) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            Text(text = "#" + hashtag.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            if (viewModel.hashtagState.hashtag != null) {
                Text(
                    text = viewModel.hashtagState.hashtag!!.count!!.toString() + " " + stringResource(
                        id = R.string.posts
                    )
                )
            }
        }

        LazyRow(contentPadding = PaddingValues(horizontal = 10.dp)) {
            items(viewModel.postsState.posts, key = {
                it.id
            }) { item ->
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .padding(horizontal = 2.dp)
                ) {
                    CustomPost(post = item, navController = navController)
                }
            }

            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(150.dp)
                        .padding(horizontal = 18.dp)
                ) {
                    Button(onClick = {
                        Navigate.navigate("hashtag_timeline_screen/${hashtag.name}", navController)
                    }) {
                        Text(text = stringResource(R.string.view_more))
                    }
                }
            }
        }

    }
}