package com.daniebeler.pixels.ui.composables.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixels.R
import com.daniebeler.pixels.domain.model.Notification
import com.daniebeler.pixels.ui.composables.ErrorComposable
import com.daniebeler.pixels.ui.composables.LoadingComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsComposable(
    navController: NavController,
    viewModel: NotificationsViewModel = hiltViewModel()
) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.notifications))
                }
            )

        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            LazyColumn(
                content = {
                    items(viewModel.notificationsState.notifications, key = {
                        it.id
                    }) {
                        CustomNotificaiton(notification = it, navController = navController)
                    }
                })
            
            LoadingComposable(isLoading = viewModel.notificationsState.isLoading)
            ErrorComposable(message = viewModel.notificationsState.error)
        }

    }


}

@Composable
fun CustomNotificaiton(notification: Notification, navController: NavController) {

    var showImage = false
    var text = ""
    if (notification.type == "follow") {
        text = " " + stringResource(R.string.followed_you)
    } else if (notification.type == "favourite") {
        text = " " + stringResource(R.string.liked_your_post)
        showImage = true
    } else if (notification.type == "reblog") {
        text = " " + stringResource(R.string.reblogged_your_post)
        showImage = true
    }

    Row(
        Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = notification.account.avatar, contentDescription = "",
            modifier = Modifier
                .height(32.dp).width(32.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    navController.navigate("profile_screen/" + notification.account.id) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Text(text = notification.account.username, fontWeight = FontWeight.Bold)

                Text(text = text)
            }


            Text(text = notification.timeAgo, fontSize = 12.sp)
        }

        if (showImage) {
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = notification.post?.mediaAttachments?.get(0)?.previewUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(36.dp)
                    .aspectRatio(1f)
            )
        }

    }

}