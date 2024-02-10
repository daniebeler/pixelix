package com.daniebeler.pixelix.ui.composables.single_post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.post.PostComposable
import com.daniebeler.pixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pixelix.ui.composables.states.LoadingComposable
import com.daniebeler.pixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SinglePostComposable(
    navController: NavController,
    postId: String,
    viewModel: SinglePostViewModel = hiltViewModel(key = postId)
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getPost(postId)
    }

    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Text(
                text = (stringResource(R.string.post_by) + " " + (viewModel.postState.post?.account?.acct
                    ?: "")), overflow = TextOverflow.Ellipsis, maxLines = 1
            )
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ""
                )
            }
        })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                if (viewModel.postState.post != null) {
                    PostComposable(viewModel.postState.post!!, navController, postGetsDeleted = {
                        Navigate().navigateAndDeleteBackStack("own_profile_screen", navController)
                    })
                }
            }

            LoadingComposable(isLoading = viewModel.postState.isLoading)
            ErrorComposable(message = viewModel.postState.error)
        }
    }
}