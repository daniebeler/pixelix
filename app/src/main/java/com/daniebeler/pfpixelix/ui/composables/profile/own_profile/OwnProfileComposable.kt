package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniebeler.pfpixelix.di.injectViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pfpixelix.ui.composables.profile.CollectionsComposable
import com.daniebeler.pfpixelix.ui.composables.profile.PostsWrapperComposable
import com.daniebeler.pfpixelix.ui.composables.profile.ProfileTopSection
import com.daniebeler.pfpixelix.ui.composables.profile.SwitchViewComposable
import com.daniebeler.pfpixelix.ui.composables.profile.server_stats.DomainSoftwareComposable
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OwnProfileComposable(
    navController: NavController,
    openPreferencesDrawer: () -> Unit,
    viewModel: OwnProfileViewModel = injectViewModel(key = "own-profile-key") { ownProfileViewModel }
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(0) }

    val lazyGridState = rememberLazyListState()

    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    LaunchedEffect(Unit) {
        viewModel.getAppIcon(context)
    }

    Scaffold(contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top), topBar = {
        CenterAlignedTopAppBar(title = {
            Row(Modifier.clickable { showBottomSheet = 2 }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = viewModel.accountState.account?.username ?: "",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = viewModel.ownDomain, fontSize = 12.sp, lineHeight = 6.sp
                    )
                }
            }
        }, actions = {
            if (viewModel.ownDomain.isNotEmpty()) {
                DomainSoftwareComposable(
                    domain = viewModel.ownDomain
                )
            }

            IconButton(onClick = {
                showBottomSheet = 1
            }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert, contentDescription = "preferences"
                )
            }
        })

    }

    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = viewModel.accountState.refreshing || viewModel.postsState.refreshing,
            onRefresh = { viewModel.loadData(true) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                state = lazyGridState
            ) {
                item {
                    Column {
                        if (viewModel.accountState.account != null) {
                            ProfileTopSection(account = viewModel.accountState.account,
                                relationship = null,
                                navController,
                                openUrl = { url ->
                                    viewModel.openUrl(url, context)
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
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    Text(text = stringResource(id = R.string.edit_profile))
                                }
                            }
                        }

                        CollectionsComposable(
                            collectionsState = viewModel.collectionsState,
                            getMoreCollections = {
                                viewModel.accountState.account?.let {
                                    viewModel.getCollections(
                                        it.id,
                                        true
                                    )
                                }
                            },
                            navController = navController,
                            addNewButton = true,
                            instanceDomain = viewModel.ownDomain,
                        ) { url -> viewModel.openUrl(url, context) }

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
                    emptyState = EmptyState(
                        icon = Icons.Outlined.Photo, heading = "No Posts"
                    ),
                    view = viewModel.view,
                    postGetsDeleted = { viewModel.postGetsDeleted(it) },
                    isFirstImageLarge = true,
                    screenWidth = screenWidth
                )

            }

            if (viewModel.postsState.posts.isEmpty() && viewModel.postsState.error.isNotBlank()) {
                FullscreenErrorComposable(message = viewModel.postsState.error)
            }
        }

    }

    InfiniteListHandler(lazyListState = lazyGridState) {
        viewModel.getPostsPaginated()
    }

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
                    }, openPreferencesDrawer)
            } else if (showBottomSheet == 2) {
                AccountSwitchBottomSheet(closeBottomSheet = {
                    showBottomSheet = 0
                }, viewModel)
            }
        }
    }
}
