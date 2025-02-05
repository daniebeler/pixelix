package com.daniebeler.pfpixelix.ui.composables.explore.trending.trending_hashtags

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniebeler.pfpixelix.di.injectViewModel
import androidx.navigation.NavController
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*
import com.daniebeler.pfpixelix.domain.model.Tag
import com.daniebeler.pfpixelix.ui.composables.CustomPost
import com.daniebeler.pfpixelix.utils.Navigate
import java.util.Locale

@Composable
fun TrendingHashtagElement(
    hashtag: Tag,
    navController: NavController,
    viewModel: TrendingHashtagElementViewModel = injectViewModel(key = "the" + hashtag.name) { trendingHashtagElementViewModel }
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            Text(text = "#" + hashtag.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            if (viewModel.hashtagState.hashtag != null) {
                Text(
                    text = "  • " + String.format(
                        Locale.GERMANY, "%,d", viewModel.hashtagState.hashtag!!.count!!
                    ) + " " + stringResource(
                        Res.string.posts
                    ), fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        LazyHorizontalGrid(
            rows = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.height(428.dp)
        ) {

            item(span = { GridItemSpan(3) }) {
                Spacer(Modifier.width(12.dp))
            }

            itemsIndexed(viewModel.postsState.posts) { index, post ->

                val postsCount = viewModel.postsState.posts.size;

                val baseModifier = Modifier

                val customModifier = when {
                    // Case for a single row
                    postsCount <= 3 -> {
                        when (index) {
                            0 -> baseModifier.clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)) // Top-left corner
                            2 -> baseModifier.clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)) // Bottom-left corner
                            else -> baseModifier // Fallback for safety
                        }
                    }
                    // Cases for multiple rows
                    index == 0 -> baseModifier.clip(RoundedCornerShape(topStart = 12.dp)) // Top-left corner
                    index == 2 -> baseModifier.clip(RoundedCornerShape(bottomStart = 12.dp)) // Bottom-start corner
                    index == postsCount - 1 && postsCount % 3 == 0 -> baseModifier.clip(
                        RoundedCornerShape(bottomEnd = 12.dp)
                    ) // Bottom-right corner
                    index >= postsCount - 3 && index % 3 == 0 -> baseModifier.clip(
                        RoundedCornerShape(topEnd = 12.dp)
                    ) // Top-right corner
                    else -> baseModifier
                }

                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(140.dp)
                ) {
                    CustomPost(post = post, navController = navController, customModifier = customModifier)
                }
            }

            item(span = { GridItemSpan(3) }) {
                Spacer(Modifier.width(12.dp))
            }
        }

    }
}