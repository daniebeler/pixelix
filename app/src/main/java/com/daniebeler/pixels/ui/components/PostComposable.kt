package com.daniebeler.pixels.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.daniebeler.pixels.models.api.Post

@Composable
fun PostComposable(post: Post) {

    Column {
        Row (verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = post.account.avatar, contentDescription = "",
                modifier = Modifier
                    .height(32.dp)
                    .clip(CircleShape)
            )
            Text(text = post.account.username)
        }

        Spacer(modifier = Modifier.height(24.dp))

        AsyncImage(model = post.mediaAttachments[0].url, contentDescription = "",
            Modifier.fillMaxWidth())
        
    }
}