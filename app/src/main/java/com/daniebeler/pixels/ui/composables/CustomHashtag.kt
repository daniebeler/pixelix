package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixels.R
import com.daniebeler.pixels.domain.model.Tag

@Composable
fun CustomHashtag(hashtag: Tag, navController: NavController) {
    Row(
        Modifier
            .padding(vertical = 12.dp, horizontal = 5.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("hashtag_timeline_screen/${hashtag.name}") {
                    launchSingleTop = true
                    restoreState = true
                }
            }) {

        /*Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .clip(CircleShape)
        )*/
        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(text = "#" + hashtag.name)
            Text(
                text = hashtag.count.toString() + " posts",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}