package com.daniebeler.pfpixelix.ui.composables.mention

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.ui.composables.post.PostComposable
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.LoadingComposable
import com.daniebeler.pfpixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentionComposable(
    mentionId: String,
    navController: NavController,
    viewModel: MentionViewModel = hiltViewModel(key = "mention$mentionId")
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadData(mentionId)
    }

    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        CenterAlignedTopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(id = R.string.post), fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(
                        id = R.string.by, (viewModel.postState.post?.account?.username ?: "")
                    ), fontSize = 12.sp, lineHeight = 6.sp
                )
            }
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos, contentDescription = ""
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
                viewModel.postContextState.postContext?.ancestors?.let { ancestors ->
                    SubPosts(ancestors, navController)
                }
                Spacer(Modifier.height(32.dp))
                viewModel.postState.post?.let { post ->
                    PostComposable(viewModel.postState.post!!, navController, postGetsDeleted = {
                        Navigate.navigateAndDeleteBackStack("own_profile_screen", navController)
                    }, setZindex = { }, openReplies = false, showReplies = false
                    )
                }
                Spacer(Modifier.height(32.dp))
                viewModel.postContextState.postContext?.descendants?.let { descendants ->
                    SubPosts(descendants, navController)
                }
            }

            LoadingComposable(isLoading = viewModel.postState.isLoading)
            ErrorComposable(message = viewModel.postState.error)
        }
    }
}

@Composable
private fun SubPosts(posts: List<Post>, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        VerticalDivider(color = MaterialTheme.colorScheme.secondary)
        Column {
            posts.map { ancestor ->
                PostComposable(ancestor, navController, postGetsDeleted = {
                    Navigate.navigateAndDeleteBackStack(
                        "own_profile_screen", navController
                    )
                }, setZindex = { }, openReplies = false, showReplies = false, modifier = Modifier.clickable {
                    Navigate.navigate("mention/" + ancestor.id, navController)
                })
            }
        }

    }
}