package com.daniebeler.pixels.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.daniebeler.pixels.MainViewModel
import com.daniebeler.pixels.models.api.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingPostsComposable(viewModel: MainViewModel) {
    val dailyTrendingPosts = viewModel.dailyTrendingPosts
    val monthlyTrendingPosts = viewModel.monthlyTrendingPosts
    val yearlyTrendingPosts = viewModel.yearlyTrendingPosts

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text("Trending")
                }
            )

        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(paddingValues),
            content = {
                item (
                    span = { GridItemSpan(3) }
                ) {
                    Heading(text = "Trending today")
                }
                items(dailyTrendingPosts) { photo ->
                    AsyncImage(
                        model = photo.mediaAttachments[0].previewUrl,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.aspectRatio(1f)
                    )
                }

                item (
                    span = { GridItemSpan(3) }
                ) {
                    Heading(text = "Trending this month")
                }
                items(monthlyTrendingPosts) { photo ->
                    AsyncImage(
                        model = photo.mediaAttachments[0].previewUrl,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.aspectRatio(1f)
                    )
                }


                item (
                    span = { GridItemSpan(3) }
                ) {
                    Heading(text = "Trending this year")
                }
                items(yearlyTrendingPosts) { photo ->
                    AsyncImage(
                        model = photo.mediaAttachments[0].previewUrl,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.aspectRatio(1f)
                    )
                }
            }
        )
    }



}

@Composable
fun Heading(text: String) {
    Text(text, fontSize = 32.sp, modifier = Modifier.padding(top = 24.dp, bottom = 6.dp))
}
