package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.ui.composables.CustomPost
import com.daniebeler.pfpixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pfpixelix.ui.composables.InfiniteGridHandler
import com.daniebeler.pfpixelix.ui.composables.InfinitePostsGrid
import com.daniebeler.pfpixelix.ui.composables.InfinitePostsList
import com.daniebeler.pfpixelix.ui.composables.post.PostComposable
import com.daniebeler.pfpixelix.ui.composables.profile.AccountState
import com.daniebeler.pfpixelix.ui.composables.profile.DomainSoftwareComposable
import com.daniebeler.pfpixelix.ui.composables.profile.PostsState
import com.daniebeler.pfpixelix.ui.composables.profile.PostsWrapperComposable
import com.daniebeler.pfpixelix.ui.composables.profile.ProfileTopSection
import com.daniebeler.pfpixelix.ui.composables.profile.SwitchViewComposable
import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pfpixelix.ui.composables.states.FixedHeightEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FixedHeightLoadingComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OwnProfileComposable(
    navController: NavController, viewModel: OwnProfileViewModel = hiltViewModel()
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val lazyGridState = rememberLazyGridState()
    val pullRefreshState = rememberPullRefreshState(refreshing = viewModel.accountState.isLoading,
        onRefresh = { viewModel.loadData() })

    val context = LocalContext.current

    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = viewModel.accountState.account?.username ?: "")
                    Text(
                        text = viewModel.ownDomain,
                        fontSize = 12.sp,
                        lineHeight = 6.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }, actions = {
            if (viewModel.domainSoftwareState.domainSoftware != null) {
                DomainSoftwareComposable(domainSoftware = viewModel.domainSoftwareState.domainSoftware!!,
                    { url -> viewModel.openUrl(context, url) })
            }

            IconButton(onClick = {
                //Navigate.navigate("settings_screen", navController)
                showBottomSheet = true
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
                modifier = Modifier.pullRefresh(pullRefreshState)
            ) {
                item(span = { GridItemSpan(3) }) {
                    Column {
                        if (viewModel.accountState.account != null) {
                            ProfileTopSection(account = viewModel.accountState.account,
                                navController,
                                openUrl = { url ->
                                    viewModel.openUrl(context, url)
                                })
                        }

                        SwitchViewComposable(postsCount = viewModel.accountState.account?.postsCount
                            ?: 0,
                            viewType = viewModel.view,
                            onViewChange = { viewModel.changeView(it) })
                    }
                }

                PostsWrapperComposable(
                    accountState = viewModel.accountState,
                    postsState = viewModel.postsState,
                    navController = navController,
                    refresh = {
                        viewModel.loadData()
                    },
                    getPostsPaginated = {
                        viewModel.getPostsPaginated()
                    },
                    emptyState = EmptyState(
                        icon = Icons.Outlined.Photo, heading = "fief", message = "fief"
                    ),
                    view = viewModel.view
                )

            }

            if (viewModel.postsState.posts.isEmpty() && viewModel.postsState.error.isNotBlank()) {
                FullscreenErrorComposable(message = viewModel.postsState.error)
            }

            /*if (before == null && items.isEmpty()) {
                if (isLoading && !isRefreshing) {
                    FullscreenLoadingComposable()
                }

                if (!isLoading && error.isEmpty()) {
                    FullscreenEmptyStateComposable(emptyMessage)
                }
            }*/
        }

        InfiniteGridHandler(lazyGridState = lazyGridState) {
            viewModel.getPostsPaginated()
        }

        CustomPullRefreshIndicator(
            viewModel.postsState.isLoading,
            pullRefreshState,
        )


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

fun LazyGridScope.PostsGridInScope(
    items: List<Post>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    error: String,
    endReached: Boolean = false,
    emptyMessage: EmptyState,
    navController: NavController,
    getItemsPaginated: () -> Unit = { },
    before: @Composable (() -> Unit)? = null,
    onRefresh: () -> Unit = { },
) {

    if (before != null) {
        item(span = { GridItemSpan(3) }) {
            before()
        }
    }

    items(items, key = {
        it.id
    }) { photo ->
        CustomPost(post = photo, navController = navController)
    }

    if (endReached && items.size > 10) {
        item(span = { GridItemSpan(3) }) {
            EndOfListComposable()
        }
    }

    if (before != null) {
        if (isLoading) {
            item(span = { GridItemSpan(3) }) {
                FixedHeightLoadingComposable()
            }
        }


        if (items.isEmpty()) {
            if (!isLoading && error.isEmpty()) {
                item(span = { GridItemSpan(3) }) {
                    FixedHeightEmptyStateComposable(emptyMessage)
                }
            }
        }
    }
}

fun LazyGridScope.PostsListInScope(
    items: List<Post>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    error: String,
    endReached: Boolean,
    emptyMessage: EmptyState,
    navController: NavController,
    getItemsPaginated: () -> Unit,
    onRefresh: () -> Unit,
    itemGetsDeleted: (postId: String) -> Unit,
    before: @Composable (() -> Unit)? = null,
) {

    if (before != null) {
        item {
            before()
        }
    }
    if (items.isNotEmpty()) {
        items(items, span = { GridItemSpan(3) }, key = {
            it.id
        }) { item ->
            PostComposable(
                post = item, postGetsDeleted = { }, navController = navController
            )
        }

        if (isLoading && !isRefreshing) {
            item {
                FixedHeightLoadingComposable()
            }
        }

        if (endReached && items.size > 3) {
            item {
                EndOfListComposable()
            }
        }
    }
}

@Composable
fun CustomProfilePageGrid(
    accountState: AccountState,
    postsState: PostsState,
    navController: NavController,
    emptyState: EmptyState,
    refresh: () -> Unit,
    getPostsPaginated: () -> Unit,
    openUrl: (url: String) -> Unit,
    otherAccountTopSectionAdditions: @Composable () -> Unit,
    changeView: (ViewEnum) -> Unit,
    view: ViewEnum
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
                    /*ProfileTopSection(
                        account = accountState.account, navController, openUrl = { url ->
                            openUrl(url)
                        }, changeView, view = view
                    )*/

                    otherAccountTopSectionAdditions()
                }

            }) { refresh() }
    }
}

@Composable
fun CustomProfilePageTimeline(
    accountState: AccountState,
    postsState: PostsState,
    navController: NavController,
    emptyState: EmptyState,
    refresh: () -> Unit,
    getPostsPaginated: () -> Unit,
    openUrl: (url: String) -> Unit,
    otherAccountTopSectionAdditions: @Composable () -> Unit,
    changeView: (ViewEnum) -> Unit,
    view: ViewEnum

) {

    Box {
        InfinitePostsList(items = postsState.posts,
            isLoading = postsState.isLoading,
            isRefreshing = accountState.isLoading && accountState.account != null,
            error = postsState.error,
            emptyMessage = emptyState,
            endReached = postsState.endReached,
            navController = navController,
            getItemsPaginated = { getPostsPaginated() },
            onRefresh = { refresh() },
            itemGetsDeleted = {},
            before = {
                Column {
                    ProfileTopSection(account = accountState.account,
                        navController,
                        openUrl = { url ->
                            openUrl(url)
                        })
                    otherAccountTopSectionAdditions()
                }
            })
    }
}