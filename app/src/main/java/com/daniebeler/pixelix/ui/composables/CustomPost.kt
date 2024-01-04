package com.daniebeler.pixelix.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixelix.domain.model.Post

@Composable
fun CustomPost(post: Post, navController: NavController) {
    if (post.sensitive) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = {
                    navController.navigate("single_post_screen/" + post.id) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }),
        ) {
            Icon(
                imageVector = Icons.Outlined.VisibilityOff,
                contentDescription = null,
                Modifier.size(50.dp)
            )
        }
    } else {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceBright)) {
            AsyncImage(
                model = post.mediaAttachments[0].previewUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable(onClick = {
                        navController.navigate("single_post_screen/" + post.id) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            )
        }
    }
}