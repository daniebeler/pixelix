package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SinglePostComposable(navController: NavController, postId: String, viewModel: SinglePostViewModel = hiltViewModel()) {

    viewModel.loadPost(postId)

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Single Post")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )

        }
    ) {paddingValues ->
        Column (Modifier.padding(paddingValues)) {
            if (viewModel.post != null) {
                PostComposable(viewModel.post!!, navController)
            }
        }
    }
}