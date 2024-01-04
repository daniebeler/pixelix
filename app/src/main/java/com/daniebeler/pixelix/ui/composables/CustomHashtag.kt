package com.daniebeler.pixelix.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.daniebeler.pixelix.domain.model.Tag

@Composable
fun CustomHashtag(hashtag: Tag, navController: NavController) {
    Row(
        Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("hashtag_timeline_screen/${hashtag.name}") {
                    launchSingleTop = true
                    restoreState = true
                }
            }) {
        Box(
            modifier = Modifier
                .height(46.dp)
                .width(46.dp)
                .background(MaterialTheme.colorScheme.surfaceBright, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Tag,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(text = "#" + hashtag.name)
            if (hashtag.count != null) {
                Text(
                    text = hashtag.count.toString() + " posts",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = hashtag.total.toString() + " people are talking",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}