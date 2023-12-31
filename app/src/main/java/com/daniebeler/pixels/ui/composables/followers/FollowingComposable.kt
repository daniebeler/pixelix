package com.daniebeler.pixels.ui.composables.followers

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixels.ui.composables.ErrorComposable
import com.daniebeler.pixels.ui.composables.LoadingComposable

@Composable
fun FollowingComposable(navController: NavController, viewModel: FollowersViewModel = hiltViewModel()) {

    LazyColumn(content = {
        items(viewModel.followingState.following, key = {
            it.id
        }) {
            CustomFollowerElement(account = it, null, navController)
        }
    })

    LoadingComposable(isLoading = viewModel.followingState.isLoading)
    ErrorComposable(message = viewModel.followingState.error)
}