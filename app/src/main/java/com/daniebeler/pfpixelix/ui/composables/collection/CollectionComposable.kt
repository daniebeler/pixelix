package com.daniebeler.pfpixelix.ui.composables.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.daniebeler.pfpixelix.ui.composables.InfinitePostsGrid
import com.daniebeler.pfpixelix.ui.composables.post.CustomBottomSheetElement
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.utils.Share

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionComposable(
    navController: NavController,
    collectionId: String,
    viewModel: CollectionViewModel = hiltViewModel()
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadData(collectionId)
    }

    Scaffold(contentWindowInsets = WindowInsets(0.dp), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            if (viewModel.collectionState.collection != null) {
                Column {
                    Text(
                        viewModel.collectionState.collection!!.title, fontWeight = FontWeight.Bold
                    )
                    Text(
                        "By " + viewModel.collectionState.collection!!.username, fontSize = 12.sp
                    )
                }
            }
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ""
                )
            }
        }, actions = {
            IconButton(onClick = {
                //Navigate.navigate("settings_screen", navController)
                showBottomSheet = true
            }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert, contentDescription = ""
                )
            }
        })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            InfinitePostsGrid(items = viewModel.collectionPostsState.posts,
                isLoading = viewModel.collectionPostsState.isLoading,
                isRefreshing = viewModel.collectionPostsState.isRefreshing,
                error = viewModel.collectionPostsState.error,
                emptyMessage = EmptyState(
                    icon = Icons.Outlined.FavoriteBorder, heading = "Empty Collection"
                ),
                navController = navController,
                getItemsPaginated = {
                    //viewModel.getItemsPaginated()
                },
                onRefresh = {
                    viewModel.refresh()
                })
        }


        if (showBottomSheet) {
            ModalBottomSheet(
                windowInsets = WindowInsets.navigationBars, onDismissRequest = {
                    showBottomSheet = false
                }, sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {

                    CustomBottomSheetElement(icon = Icons.Outlined.OpenInBrowser,
                        text = stringResource(
                            R.string.open_in_browser
                        ),
                        onClick = {
                            if (viewModel.collectionState.collection != null) {
                                viewModel.openUrl(
                                    context, viewModel.collectionState.collection!!.url
                                )
                            }
                        })

                    CustomBottomSheetElement(icon = Icons.Outlined.Share,
                        text = stringResource(R.string.share_this_collection),
                        onClick = {
                            if (viewModel.collectionState.collection != null) {
                                Share.shareText(context, viewModel.collectionState.collection!!.url)
                            }
                        })
                }

            }

        }
    }
}