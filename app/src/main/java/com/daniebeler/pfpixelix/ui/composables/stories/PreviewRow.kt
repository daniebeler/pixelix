package com.daniebeler.pfpixelix.ui.composables.stories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.domain.model.StoryUser

@Composable
fun PreviewRow(viewModel: PreviewRowViewModel = hiltViewModel(key = "preview-row-vm")) {

    LazyRow (modifier = Modifier.padding(top = 12.dp), contentPadding = PaddingValues(horizontal = 12.dp)) {
        if (viewModel.storiesCarousel.carousel != null) {
            item {
                Story(account = viewModel.storiesCarousel.carousel!!.self.user)
            }

            items(viewModel.storiesCarousel.carousel!!.nodes) {
                Story(account = it.user)
            }
        }
    }

}

@Composable
private fun Story(account: StoryUser) {
    Column {
        AsyncImage(
            model = account.avatar, contentDescription = "", modifier = Modifier.clip(
                CircleShape
            )
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = account.username, color = MaterialTheme.colorScheme.onSurface)
    }
}