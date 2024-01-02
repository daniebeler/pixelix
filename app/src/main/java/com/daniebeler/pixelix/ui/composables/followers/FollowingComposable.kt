package com.daniebeler.pixelix.ui.composables.followers

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.CustomAccount
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.LoadingComposable

@Composable
fun FollowingComposable(navController: NavController, viewModel: FollowersViewModel = hiltViewModel()) {

    LazyColumn(content = {
        items(viewModel.followingState.following, key = {
            it.id
        }) {
            CustomAccount(account = it, null, navController)
        }
    })

    LoadingComposable(isLoading = viewModel.followingState.isLoading)
    ErrorComposable(message = viewModel.followingState.error)
}