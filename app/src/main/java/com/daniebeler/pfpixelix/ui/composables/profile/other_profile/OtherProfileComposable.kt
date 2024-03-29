package com.daniebeler.pfpixelix.ui.composables.profile.other_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Share
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.FollowButton
import com.daniebeler.pfpixelix.ui.composables.post.CustomBottomSheetElement
import com.daniebeler.pfpixelix.ui.composables.profile.DomainSoftwareComposable
import com.daniebeler.pfpixelix.ui.composables.profile.MutualFollowersComposable
import com.daniebeler.pfpixelix.ui.composables.profile.own_profile.CustomProfilePage
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.utils.Share

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherProfileComposable(
    navController: NavController,
    userId: String,
    viewModel: OtherProfileViewModel = hiltViewModel(key = "other-profile$userId")
) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadData(userId)
    }


    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Row {
                Column {

                    Text(text = viewModel.accountState.account?.username ?: "")
                    Text(
                        text = viewModel.domain,
                        fontSize = 12.sp,
                        lineHeight = 6.sp,
                        color = MaterialTheme.colorScheme.primary
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

            if (viewModel.domainSoftwareState.domainSoftware != null) {
                DomainSoftwareComposable(
                    domainSoftware = viewModel.domainSoftwareState.domainSoftware!!,
                    { url -> viewModel.openUrl(context, url) })
            }

            IconButton(onClick = {
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

            CustomProfilePage(accountState = viewModel.accountState,
                postsState = viewModel.postsState,
                navController = navController,
                refresh = {
                    viewModel.loadData(userId)
                },
                getPostsPaginated = {
                    viewModel.getPostsPaginated(userId)
                },
                openUrl = { viewModel.openUrl(context, it) },
                emptyState = EmptyState(
                    icon = Icons.Outlined.Photo,
                    heading = stringResource(R.string.no_posts_yet),
                    message = stringResource(R.string.this_user_has_not_postet_anything_yet)
                ),
                otherAccountTopSectionAdditions = {
                    Column(Modifier.padding(12.dp)) {

                        MutualFollowersComposable(viewModel.mutualFollowersState)

                        if (viewModel.mutualFollowersState.mutualFollowers.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        FollowButton(firstLoaded = viewModel.relationshipState.accountRelationship != null,
                            isLoading = viewModel.relationshipState.isLoading,
                            isFollowing = viewModel.relationshipState.accountRelationship?.following
                                ?: false,
                            onFollowClick = { viewModel.followAccount(userId) },
                            onUnFollowClick = { viewModel.unfollowAccount(userId) })

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                })
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }, sheetState = sheetState
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
                    viewModel.openUrl(context, viewModel.accountState.account!!.url)
                })

                CustomBottomSheetElement(icon = Icons.Outlined.Share,
                    text = stringResource(R.string.share_this_profile),
                    onClick = {
                        Share.shareText(context, viewModel.accountState.account!!.url)
                    })
            }
        }
    }
}