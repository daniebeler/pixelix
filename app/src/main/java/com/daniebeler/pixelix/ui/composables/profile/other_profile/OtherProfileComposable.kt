package com.daniebeler.pixelix.ui.composables.profile.other_profile

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.InfinitePostsGrid
import com.daniebeler.pixelix.ui.composables.post.CustomBottomSheetElement
import com.daniebeler.pixelix.ui.composables.profile.MutualFollowersComposable
import com.daniebeler.pixelix.ui.composables.profile.ProfileTopSection
import com.daniebeler.pixelix.utils.Navigate
import com.daniebeler.pixelix.utils.Share

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherProfileComposable(
    navController: NavController,
    userId: String,
    viewModel: OtherProfileViewModel = hiltViewModel(key = userId)
) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadData(userId)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = viewModel.accountState.account?.username ?: "")
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
                },
                actions = {
                    IconButton(onClick = {
                        showBottomSheet = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = ""
                        )
                    }
                }
            )

        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            if (viewModel.accountState.account != null) {

                InfinitePostsGrid(
                    items = viewModel.postsState.posts,
                    isLoading = viewModel.postsState.isLoading,
                    isRefreshing = false,
                    error = viewModel.postsState.error,
                    endReached = viewModel.postsState.endReached,
                    emptyMessage = {
                        Image(
                            painter = painterResource(id = R.drawable.empty_state_no_posts),
                            contentDescription = null,
                            Modifier.fillMaxWidth()
                        )
                    },
                    navController = navController,
                    getItemsPaginated = { viewModel.getPostsPaginated(userId) },
                    before = {

                        Column {
                            ProfileTopSection(viewModel.accountState.account!!, navController)


                            Column(Modifier.padding(12.dp)) {

                                MutualFollowersComposable(viewModel.mutualFollowersState)

                                Spacer(modifier = Modifier.height(12.dp))

                                if (viewModel.relationshipState.isLoading) {
                                    Button(onClick = {
                                        viewModel.unfollowAccount(userId)
                                    }, modifier = Modifier.width(120.dp)) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = MaterialTheme.colorScheme.secondary,
                                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                        )
                                    }
                                }

                                if (viewModel.relationshipState.accountRelationship != null) {
                                    if (viewModel.relationshipState.accountRelationship!!.following) {
                                        Button(
                                            onClick = {
                                                viewModel.unfollowAccount(userId)
                                            },
                                            modifier = Modifier.width(120.dp)
                                        ) {
                                            Text(text = stringResource(R.string.unfollow))
                                        }
                                    } else {
                                        Button(onClick = {
                                            viewModel.followAccount(userId)
                                        }, modifier = Modifier.width(120.dp)) {
                                            Text(text = stringResource(R.string.follow))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    },
                    onRefresh = {
                        viewModel.loadData(userId)
                    })
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                if (viewModel.relationshipState.accountRelationship != null) {
                    if (viewModel.relationshipState.accountRelationship!!.muting) {
                        CustomBottomSheetElement(icon = Icons.Outlined.DoNotDisturbOn,
                            text = stringResource(
                                R.string.unmute_this_profile
                            ),
                            onClick = {
                                viewModel.unMuteAccount(userId)
                            })
                    } else {
                        CustomBottomSheetElement(icon = Icons.Outlined.DoNotDisturbOn,
                            text = stringResource(
                                R.string.mute_this_profile
                            ),
                            onClick = {
                                viewModel.muteAccount(userId)
                            })
                    }

                    if (viewModel.relationshipState.accountRelationship!!.blocking) {
                        CustomBottomSheetElement(icon = Icons.Outlined.Block, text = stringResource(
                            R.string.unblock_this_profile
                        ), onClick = {
                            viewModel.unblockAccount(userId)
                        })
                    } else {
                        CustomBottomSheetElement(icon = Icons.Outlined.Block, text = stringResource(
                            R.string.block_this_profile
                        ), onClick = {
                            viewModel.blockAccount(userId)
                        })
                    }
                }

                HorizontalDivider(Modifier.padding(12.dp))

                CustomBottomSheetElement(icon = Icons.Outlined.OpenInBrowser, text = stringResource(
                    R.string.open_in_browser
                ), onClick = {
                    Navigate().openUrlInApp(context, viewModel.accountState.account!!.url)
                })

                CustomBottomSheetElement(
                    icon = Icons.Outlined.Share,
                    text = stringResource(R.string.share_this_profile),
                    onClick = {
                        Share().shareText(context, viewModel.accountState.account!!.url)
                    })
            }
        }
    }
}



