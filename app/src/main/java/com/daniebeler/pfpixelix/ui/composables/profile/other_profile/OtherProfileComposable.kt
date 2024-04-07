package com.daniebeler.pfpixelix.ui.composables.profile.other_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.ui.composables.FollowButton
import com.daniebeler.pfpixelix.ui.composables.post.CustomBottomSheetElement
import com.daniebeler.pfpixelix.ui.composables.profile.DomainSoftwareComposable
import com.daniebeler.pfpixelix.ui.composables.profile.MutualFollowersComposable
import com.daniebeler.pfpixelix.ui.composables.profile.own_profile.CustomProfilePage
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.utils.Navigate
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
    var showMuteAlert by remember { mutableStateOf(false) }
    var showUnMuteAlert by remember { mutableStateOf(false) }
    var showBlockAlert by remember { mutableStateOf(false) }
    var showUnBlockAlert by remember { mutableStateOf(false) }

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
                DomainSoftwareComposable(domainSoftware = viewModel.domainSoftwareState.domainSoftware!!,
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
                    Column(Modifier.padding(8.dp)) {

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

                        Spacer(modifier = Modifier.height(8.dp))
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
                        CustomBottomSheetElement(icon = Icons.AutoMirrored.Outlined.VolumeOff,
                            text = stringResource(
                                R.string.unmute_this_profile
                            ),
                            onClick = {
                                showUnMuteAlert = true
                            })
                    } else {
                        CustomBottomSheetElement(icon = Icons.AutoMirrored.Outlined.VolumeOff,
                            text = stringResource(
                                R.string.mute_this_profile
                            ),
                            onClick = {
                                showMuteAlert = true
                            })
                    }

                    if (viewModel.relationshipState.accountRelationship!!.blocking) {
                        CustomBottomSheetElement(icon = Icons.Outlined.Block, text = stringResource(
                            R.string.unblock_this_profile
                        ), onClick = {
                            showUnBlockAlert = true
                        })
                    } else {
                        CustomBottomSheetElement(icon = Icons.Outlined.Block, text = stringResource(
                            R.string.block_this_profile
                        ), onClick = {
                            showBlockAlert = true
                        })
                    }
                }

                HorizontalDivider(Modifier.padding(12.dp))

                CustomBottomSheetElement(icon = Icons.Outlined.OpenInBrowser,
                    text = "Send message",
                    onClick = {
                        Navigate.navigate(
                            "chat/" + viewModel.accountState.account!!.id, navController
                        )
                    })

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

    if (showUnMuteAlert) {
        UnMuteAccountAlert(onDismissRequest = { showUnMuteAlert = false }, onConfirmation = {
            showUnMuteAlert = false
            viewModel.unMuteAccount(userId)
        }, account = viewModel.accountState.account!!
        )
    }
    if (showMuteAlert) {
        MuteAccountAlert(onDismissRequest = { showMuteAlert = false }, onConfirmation = {
            showMuteAlert = false
            viewModel.muteAccount(userId)
        }, account = viewModel.accountState.account!!
        )
    }
    if (showBlockAlert) {
        BlockAccountAlert(onDismissRequest = { showBlockAlert = false }, onConfirmation = {
            showBlockAlert = false
            viewModel.blockAccount(userId)
        }, account = viewModel.accountState.account!!
        )
    }
    if (showUnBlockAlert) {
        UnBlockAccountAlert(onDismissRequest = { showUnBlockAlert = false }, onConfirmation = {
            showUnBlockAlert = false
            viewModel.unblockAccount(userId)
        }, account = viewModel.accountState.account!!
        )
    }
}

@Composable
fun MuteAccountAlert(
    onDismissRequest: () -> Unit, onConfirmation: () -> Unit, account: Account
) {
    AlertDialog(title = {
        Text(text = stringResource(R.string.mute_account))
    }, text = {
        Column {

            AlertTopSection(account = account)

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            Text(text = stringResource(R.string.mute_consequence_1))
            Text(text = stringResource(R.string.mute_consequence_2))
            Text(text = stringResource(R.string.mute_consequence_3))
            Text(text = stringResource(R.string.mute_consequence_4))

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            Text(text = stringResource(R.string.mute_consequence_5))

        }
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onConfirmation()
        }) {
            Text(stringResource(R.string.mute))
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(stringResource(id = R.string.cancel))
        }
    })
}

@Composable
fun UnMuteAccountAlert(
    onDismissRequest: () -> Unit, onConfirmation: () -> Unit, account: Account
) {
    AlertDialog(title = {
        Text(text = stringResource(R.string.unmute_account))
    }, text = {
        AlertTopSection(account = account)


    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onConfirmation()
        }) {
            Text(stringResource(id = R.string.unmute_caps))
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(stringResource(id = R.string.cancel))
        }
    })
}

@Composable
fun BlockAccountAlert(
    onDismissRequest: () -> Unit, onConfirmation: () -> Unit, account: Account
) {
    AlertDialog(title = {
        Text(text = stringResource(R.string.block_account))
    }, text = {
        Column {

            AlertTopSection(account = account)

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            Text(text = stringResource(R.string.block_consequence_1))
            Text(text = stringResource(R.string.block_consequence_2))
            Text(text = stringResource(R.string.block_consequence_3))
            Text(text = stringResource(R.string.block_consequence_4))
            Text(text = stringResource(R.string.block_consequence_5))

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            Text(text = stringResource(R.string.block_consequence_6))
            Text(text = stringResource(R.string.block_consequence_7))
            Text(text = stringResource(R.string.block_consequence_8))
            Text(text = stringResource(R.string.block_consequence_9))

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            Text(text = stringResource(R.string.block_consequence_10))
        }
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onConfirmation()
        }) {
            Text(stringResource(R.string.block))
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(stringResource(id = R.string.cancel))
        }
    })

}

@Composable
fun UnBlockAccountAlert(
    onDismissRequest: () -> Unit, onConfirmation: () -> Unit, account: Account
) {
    AlertDialog(title = {
        Text(text = stringResource(id = R.string.unblock_account))
    }, text = {
        AlertTopSection(account = account)
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onConfirmation()
        }) {
            Text(stringResource(id = R.string.unblock_caps))
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(stringResource(id = R.string.cancel))
        }
    })
}

@Composable
fun AlertTopSection(account: Account) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = account.avatar,
            contentDescription = "",
            modifier = Modifier
                .height(46.dp)
                .width(46.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {

            Column {
                if (account.displayname != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = account.displayname,
                            lineHeight = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = account.username, fontSize = 12.sp)
                    Text(
                        text = " • " + (account.url.substringAfter("https://").substringBefore("/")
                            ?: ""), color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp
                    )
                }

            }
        }
    }
}