package com.daniebeler.pixels.ui.composables.followers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.ui.composables.ErrorComposable
import com.daniebeler.pixels.ui.composables.LoadingComposable

@Composable
fun FollowersComposable(navController: NavController, viewModel: FollowersViewModel = hiltViewModel()) {
    
    LazyColumn(content = {
        items(viewModel.followersState.followers, key = {
            it.id
        }) {
            CustomFollowerElement(account = it, navController)
        }
    })
    
    LoadingComposable(isLoading = viewModel.followersState.isLoading)
    ErrorComposable(message = viewModel.followersState.error)
}

@Composable
fun CustomFollowerElement(account: Account, navController: NavController) {
    Row (modifier = Modifier
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .fillMaxWidth()
        .clickable {
            navController.navigate("profile_screen/" + account.id) {
                launchSingleTop = true
                restoreState = true
            }
        }) {
        AsyncImage(
            model = account.avatar, contentDescription = "",
            modifier = Modifier
                .height(32.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))

        Text(text = account.displayname)
    }
}