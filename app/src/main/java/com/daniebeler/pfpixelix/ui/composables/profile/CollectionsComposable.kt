package com.daniebeler.pfpixelix.ui.composables.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.utils.Navigate

@Composable
fun CollectionsComposable (collectionsState: CollectionsState, navController: NavController) {
    
    LazyRow {
        items(collectionsState.collections) {
            Column (Modifier.clickable {
                Navigate.navigate("collection_screen/" + it.id, navController)
            }) {
                AsyncImage(
                    model = it.thumbnail,
                    contentDescription = "",
                    modifier = Modifier
                        .height(58.dp)
                        .width(58.dp)
                )
                Text(text = it.title)
            }
        }
    }
}