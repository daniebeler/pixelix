package com.daniebeler.pixels.ui.composables.followers

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun FollowingComposable(navController: NavController, viewModel: FollowersViewModel = hiltViewModel()) {

    LazyColumn(content = {
        items(viewModel.following, key = {
            it.id
        }) {
            CustomFollowerElement(account = it, navController)
        }
    })
}