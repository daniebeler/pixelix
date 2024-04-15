package com.daniebeler.pfpixelix.ui.composables.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.utils.Navigate

@Composable
fun CollectionsComposable(collectionsState: CollectionsState, navController: NavController) {

    Column {
        if (collectionsState.collections.isNotEmpty()) {
            HorizontalDivider(Modifier.padding(12.dp))
        }

        LazyRow {
            items(collectionsState.collections) {
                Column(
                    Modifier
                        .padding(12.dp)
                        .clickable {
                        Navigate.navigate("collection_screen/" + it.id, navController)
                    }, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = it.thumbnail,
                        contentDescription = "",
                        modifier = Modifier
                            .height(84.dp)
                            .width(84.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it.title)
                }
            }
        }
    }


}