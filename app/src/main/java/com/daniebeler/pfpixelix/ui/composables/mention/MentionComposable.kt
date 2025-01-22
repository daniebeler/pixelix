package com.daniebeler.pfpixelix.ui.composables.mention

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentionComposable(
    mentionId: String,
    navController: NavController,
    viewModel: MentionViewModel = hiltViewModel(key = "mention$mentionId")
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.loadData(mentionId)
    }

    LaunchedEffect(viewModel.postState.post, viewModel.postContextState.postContext) {
        if (viewModel.postState.post != null && viewModel.postContextState.postContext != null) {
            val index = viewModel.postContextState.postContext!!.ancestors.size + 1
            Log.d("index", index.toString())
            coroutineScope.launch {
                lazyListState.animateScrollToItem(index, scrollOffset = 0)  // Adjust index based on where the target item is
            }
        }
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
            LazyColumn(
                state = lazyListState,
            ) {
                viewModel.postContextState.postContext?.ancestors?.let { ancestors ->
                    item {
                        SubPosts(ancestors, navController)
                    }
                }
                item { Spacer(Modifier.height(32.dp)) }
                viewModel.postState.post?.let { post ->
                    item {
                        PostComposable(
                            post,
                            navController,
                            postGetsDeleted = {
                                Navigate.navigateAndDeleteBackStack("own_profile_screen", navController)
                            },
                            setZindex = { },
                            openReplies = false,
                            showReplies = false
                        )
                    }
                }
                item { Spacer(Modifier.height(32.dp)) }
                viewModel.postContextState.postContext?.descendants?.let { descendants ->
                    item {
                        SubPosts(descendants, navController)
                    }
                }
            }

            LoadingComposable(isLoading = viewModel.postState.isLoading)
            ErrorComposable(message = viewModel.postState.error)
        }
    }
}

@Composable
private fun SubPosts(posts: List<Post>, navController: NavController) {
    Column(
        Modifier.padding(start = 20.dp),
    ) {
        posts.map { ancestor ->
            PostComposable(ancestor,
                navController,
                postGetsDeleted = {
                    Navigate.navigateAndDeleteBackStack(
                        "own_profile_screen", navController
                    )
                },
                setZindex = { },
                openReplies = false,
                showReplies = false,
                modifier = Modifier.clickable {
                    Navigate.navigate("mention/" + ancestor.id, navController)
                })
        }
    }
}