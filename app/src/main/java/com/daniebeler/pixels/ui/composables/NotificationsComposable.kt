package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import com.daniebeler.pixels.MainViewModel
import com.daniebeler.pixels.api.models.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsComposable(viewModel: MainViewModel, navController: NavController) {

    val notifications = viewModel.notifications

    CoroutineScope(Dispatchers.Default).launch {
        viewModel.gotDataFromDataStore.collect { state ->
            if (state) {
                if (notifications.isEmpty()) {
                    viewModel.getNotifications()
                }
            }
        }
    }

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
            items(notifications) {
                CustomNotificaiton(notification = it, navController = navController)
            }
        })
    }


}

@Composable
fun CustomNotificaiton(notification: Notification, navController: NavController) {
    Text(text = notification.type)
}