package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.InfinitePostsGrid
import com.daniebeler.pfpixelix.ui.composables.profile.AccountState
import com.daniebeler.pfpixelix.ui.composables.profile.PostsState
import com.daniebeler.pfpixelix.ui.composables.profile.ProfileTopSection
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnProfileComposable(
    navController: NavController, viewModel: OwnProfileViewModel = hiltViewModel()
) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Column {
                Text(text = viewModel.accountState.account?.username ?: "")
                Text(
                    text = viewModel.ownDomain,
                    fontSize = 12.sp,
                    lineHeight = 6.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }, actions = {
            IconButton(onClick = {
                //Navigate().navigate("settings_screen", navController)
                showBottomSheet = true
            }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert, contentDescription = ""
                )
            }
        })

    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            Navigate().navigate("new_post_screen", navController)
        }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            CustomProfilePage(accountState = viewModel.accountState,
                postsState = viewModel.postsState,
                navController = navController,
                refresh = {
                    viewModel.loadData()
                },
                getPostsPaginated = {
                    viewModel.getPostsPaginated()
                },
                openUrl = { viewModel.openUrl(context, it) },
                emptyState = EmptyState(icon = Icons.Outlined.Photo,
                    heading = stringResource(R.string.no_posts_yet),
                    message = stringResource(R.string.upload_your_first_post),
                    buttonText = stringResource(id = R.string.new_post),
                    onClick = {
                        Navigate().navigate("new_post_screen", navController)
                    }),
                otherAccountTopSectionAdditions = {})
        }


        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                }, sheetState = sheetState
            ) {
                ModalBottomSheetContent(navController = navController,
                    instanceDomain = viewModel.ownDomain,
                    closeBottomSheet = {
                        showBottomSheet = false
                    })
            }
        }
    }
}

@Composable
fun CustomProfilePage(
    accountState: AccountState,
    postsState: PostsState,
    navController: NavController,
    emptyState: EmptyState,
    refresh: () -> Unit,
    getPostsPaginated: () -> Unit,
    openUrl: (url: String) -> Unit,
    otherAccountTopSectionAdditions: @Composable () -> Unit
) {
    Box {
        InfinitePostsGrid(items = postsState.posts,
            isLoading = postsState.isLoading,
            isRefreshing = accountState.isLoading && accountState.account != null,
            error = postsState.error,
            emptyMessage = emptyState,
            endReached = postsState.endReached,
            navController = navController,
            getItemsPaginated = { getPostsPaginated() },
            before = {
                Column {
                    ProfileTopSection(account = accountState.account,
                        navController,
                        openUrl = { url ->
                            openUrl(url)
                        })

                    otherAccountTopSectionAdditions()
                }

            }) { refresh() }
    }
}