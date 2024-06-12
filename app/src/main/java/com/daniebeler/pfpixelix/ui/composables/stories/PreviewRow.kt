package com.daniebeler.pfpixelix.ui.composables.stories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.domain.model.StoryUser

@Composable
fun PreviewRow(viewModel: PreviewRowViewModel = hiltViewModel(key = "preview-row-vm")) {

    LazyRow(
        modifier = Modifier.padding(top = 12.dp), contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        if (viewModel.storiesCarousel.carousel != null) {
            item {
                Story(account = viewModel.storiesCarousel.carousel!!.self.user, { })
            }

            items(viewModel.storiesCarousel.carousel!!.nodes) {
                Story(account = it.user) { viewModel.showStory = true }
            }
        }
    }

    if (viewModel.showStory) {
        Dialog(
            onDismissRequest = { viewModel.showStory = false }, properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            /* Your full screen content */

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow)
            ) {
                AsyncImage(
                    model = viewModel.storiesCarousel.carousel!!.nodes[0].nodes[0].src,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

            }
        }
    }


}

@Composable
private fun Story(account: StoryUser, onClick: () -> Unit) {
    Column {
        AsyncImage(model = account.avatar,
            contentDescription = "",
            modifier = Modifier
                .size(70.dp)
                .clip(
                    CircleShape
                )
                .border( // Add a border to the image
                    width = 2.dp, // The width of the border is 2dp
                    brush = Brush.linearGradient( // The color of the border is a linear gradient
                        colors = listOf(Color.Yellow, Color.Red), // The colors of the gradient
                        start = Offset(0f, 0f), // The start point of the gradient
                        end = Offset(70f, 70f) // The end point of the gradient
                    ), shape = CircleShape // The shape of the border is a circle
                )
                .clickable {
                    onClick()
                })

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = account.username, color = MaterialTheme.colorScheme.onSurface)
    }
}