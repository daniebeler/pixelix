package com.daniebeler.pfpixelix.ui.composables.profile

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CollectionsComposable (collectionsState: CollectionsState) {
    
    LazyRow {
        items(collectionsState.collections) {
            Text(text = it.title)
        }
    }
}