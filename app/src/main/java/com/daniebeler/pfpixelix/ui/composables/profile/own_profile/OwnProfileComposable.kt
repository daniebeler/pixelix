package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pfpixelix.ui.composables.InfiniteGridHandler
import com.daniebeler.pfpixelix.ui.composables.profile.CollectionsComposable
import com.daniebeler.pfpixelix.ui.composables.profile.DomainSoftwareComposable
import com.daniebeler.pfpixelix.ui.composables.profile.PostsWrapperComposable
import com.daniebeler.pfpixelix.ui.composables.profile.ProfileTopSection
import com.daniebeler.pfpixelix.ui.composables.profile.SwitchViewComposable
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OwnProfileComposable(
    navController: NavController,
    viewModel: OwnProfileViewModel = hiltViewModel(key = "own-profile-key")
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(0) }

    val lazyGridState = rememberLazyGridState()
    val pullRefreshState =
        rememberPullRefreshState(refreshing = viewModel.accountState.refreshing || viewModel.postsState.refreshing,
            onRefresh = { viewModel.loadData(true) })

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getAppIcon(context)
    }

    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Row(Modifier.clickable { showBottomSheet = 2 }) {
                        Text(
                            text = viewModel.accountState.account?.username ?: "",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "account switch dropdown",
                            Modifier.size(36.dp)
                        )
                    }
                    Text(
                        text = viewModel.ownDomain, fontSize = 12.sp, lineHeight = 6.sp
                    )
                }
            }
        }, actions = {
            if (viewModel.domainSoftwareState.domainSoftware != null) {
                DomainSoftwareComposable(
                    domainSoftware = viewModel.domainSoftwareState.domainSoftware!!
                ) { url -> viewModel.openUrl(context, url) }
            }

            IconButton(onClick = {
                showBottomSheet = 1
            }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert, contentDescription = ""
                )
            }
        })

    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            Navigate.navigate("new_post_screen", navController)
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.pullRefresh(pullRefreshState),
                state = lazyGridState
            ) {
                item(span = { GridItemSpan(3) }) {
                    Column {
                        if (viewModel.accountState.account != null) {
                            ProfileTopSection(account = viewModel.accountState.account,
                                relationship = null,
                                navController,
                                openUrl = { url ->
                                    viewModel.openUrl(context, url)
                                })

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            ) {
                                Button(
                                    onClick = {
                                        Navigate.navigate(
                                            "edit_profile_screen", navController
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                ) {
                                    Text(text = stringResource(id = R.string.edit_profile))
                                }
                            }
                        }

                        CollectionsComposable(
                            collectionsState = viewModel.collectionsState,
                            navController = navController,
                            addNewButton = true,
                            instanceDomain = viewModel.ownDomain,
                        ) { url -> viewModel.openUrl(context, url) }

                        SwitchViewComposable(postsCount = viewModel.accountState.account?.postsCount
                            ?: 0,
                            viewType = viewModel.view,
                            onViewChange = { viewModel.changeView(it) })
                    }
                }

                PostsWrapperComposable(accountState = viewModel.accountState,
                    postsState = viewModel.postsState,
                    navController = navController,
                    refresh = {
                        viewModel.loadData(true)
                    },
                    getPostsPaginated = {
                        viewModel.getPostsPaginated()
                    },
                    emptyState = EmptyState(
                        icon = Icons.Outlined.Photo, heading = "No Posts"
                    ),
                    view = viewModel.view,
                    postGetsDeleted = { viewModel.postGetsDeleted(it) })

            }

            if (viewModel.postsState.posts.isEmpty() && viewModel.postsState.error.isNotBlank()) {
                FullscreenErrorComposable(message = viewModel.postsState.error)
            }
        }
    }

    InfiniteGridHandler(lazyGridState = lazyGridState) {
        viewModel.getPostsPaginated()
    }

    CustomPullRefreshIndicator(
        viewModel.postsState.refreshing || viewModel.accountState.refreshing,
        pullRefreshState,
    )

    if (showBottomSheet > 0) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = 0
            }, sheetState = sheetState
        ) {
            if (showBottomSheet == 1) {
                ModalBottomSheetContent(navController = navController,
                    instanceDomain = viewModel.ownDomain,
                    appIcon = viewModel.appIcon,
                    closeBottomSheet = {
                        showBottomSheet = 0
                    })
            } else if (showBottomSheet == 2) {
                AccountSwitchBottomSheet(closeBottomSheet = {
                    showBottomSheet = 0
                }, viewModel)
            }
        }
    }
}