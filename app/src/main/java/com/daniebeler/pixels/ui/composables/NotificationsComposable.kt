package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixels.domain.model.Notification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsComposable(navController: NavController, viewModel: NotificationsViewModel = hiltViewModel()) {



    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Notifications")
                }
            )

        }
    ) { paddingValues ->
        LazyColumn(Modifier.padding(paddingValues),
            content = {
            items(viewModel.notifications, key = {
                it.id
            }) {
                CustomNotificaiton(notification = it, navController = navController)
            }
        })
    }


}

@Composable
fun CustomNotificaiton(notification: Notification, navController: NavController) {
    Row (
        Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        Row (verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
            navController.navigate("profile_screen/" + notification.account.id) {
                launchSingleTop = true
                restoreState = true
            }
        }
        ) {
            AsyncImage(
                model = notification.account.avatar, contentDescription = "",
                modifier = Modifier
                    .height(32.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(text = notification.account.username, fontWeight = FontWeight.Bold)
        }

        if (notification.type == "follow") {
            Text(text = " followed you")
        }
        else if (notification.type == "favourite") {
            Text(text = " liked your post")
            Spacer(modifier = Modifier.width(10.dp))
            AsyncImage(
                model = notification.post?.mediaAttachments?.get(0)?.previewUrl, contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(32.dp)
                    .aspectRatio(1f)
            )
        }

    }

}