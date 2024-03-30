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
import androidx.compose.material.icons.automirrored.outlined.Reply
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.VisibilityOff
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
                                showUnMuteAlert = true
                            })
                    } else {
                        CustomBottomSheetElement(icon = Icons.Outlined.DoNotDisturbOn,
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
        }, accountToMute = viewModel.accountState.account!!
        )
    }
    if (showMuteAlert) {
        MuteAccountAlert(onDismissRequest = { showMuteAlert = false }, onConfirmation = {
            showMuteAlert = false
            viewModel.muteAccount(userId)
        }, accountToMute = viewModel.accountState.account!!
        )
    }
    if (showBlockAlert) {
        BlockAccountAlert(onDismissRequest = { showBlockAlert = false }, onConfirmation = {
            showBlockAlert = false
            viewModel.blockAccount(userId)
        }, accountToMute = viewModel.accountState.account!!
        )
    }
    if (showUnBlockAlert) {
        UnBlockAccountAlert(onDismissRequest = { showUnBlockAlert = false }, onConfirmation = {
            showUnBlockAlert = false
            viewModel.unblockAccount(userId)
        }, accountToMute = viewModel.accountState.account!!
        )
    }
}

@Composable
fun MuteAccountAlert(
    onDismissRequest: () -> Unit, onConfirmation: () -> Unit, accountToMute: Account
) {
    AlertDialog(title = {
        Text(text = "Mute User?")
    }, text = {
        Column {

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = accountToMute.avatar,
                    contentDescription = "",
                    modifier = Modifier
                        .height(46.dp)
                        .width(46.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {

                    Column {
                        if (accountToMute.displayname != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = accountToMute.displayname,
                                    lineHeight = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = accountToMute.username, fontSize = 12.sp)
                            Text(
                                text = " • " + (accountToMute.url.substringAfter("https://")
                                    .substringBefore("/") ?: ""),
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 12.sp
                            )
                        }

                    }
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            Row {
                //Icon(imageVector = Icons.Outlined.Campaign, contentDescription = null)
                Text(text = "• They won't know they've been muted")
            }
            Row {
                //Icon(imageVector = Icons.Outlined.VisibilityOff, contentDescription = null)
                Text(text = "• They can still see your posts, but you won't see theirs")
            }
            Row {
                //Icon(imageVector = Icons.Outlined.AlternateEmail, contentDescription = null)
                Text(text = "• You won't see posts that mention them.")
            }
            Row {
                //Icon(imageVector = Icons.AutoMirrored.Outlined.Reply, contentDescription = null)
                Text(text = "• They can mention and follow you, but you won't see them.")
            }
        }
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onConfirmation()
        }) {
            Text("Mute")
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text("Cancel")
        }
    })
}

@Composable
fun UnMuteAccountAlert(
    onDismissRequest: () -> Unit, onConfirmation: () -> Unit, accountToMute: Account
) {
    AlertDialog(icon = {
        Icon(Icons.AutoMirrored.Outlined.VolumeOff, contentDescription = "Example Icon")
    }, title = {
        Text(text = "Unmute User?")
    }, text = {
        Text(text = accountToMute.acct)
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onConfirmation()
        }) {
            Text("Unmute")
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text("Cancel")
        }
    })
}

@Composable
fun BlockAccountAlert(
    onDismissRequest: () -> Unit, onConfirmation: () -> Unit, accountToMute: Account
) {
    AlertDialog(icon = {
        Icon(Icons.AutoMirrored.Outlined.VolumeOff, contentDescription = "Example Icon")
    }, title = {
        Text(text = "Block User?")
    }, text = {
        Column {
            Text(text = accountToMute.acct)
            Row {
                Icon(imageVector = Icons.Outlined.Campaign, contentDescription = null)
                Text(text = "They won't know they've been blocked")
            }
            Row {
                Icon(imageVector = Icons.Outlined.VisibilityOff, contentDescription = null)
                Text(text = "They can still see your posts, but you won't see theirs")
            }
            Row {
                Icon(imageVector = Icons.Outlined.AlternateEmail, contentDescription = null)
                Text(text = "You won't see posts that mention them.")
            }
            Row {
                Icon(imageVector = Icons.AutoMirrored.Outlined.Reply, contentDescription = null)
                Text(text = "They can mention and follow you, but you won't see them.")
            }
        }
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onConfirmation()
        }) {
            Text("Block")
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text("Cancel")
        }
    })

}

@Composable
fun UnBlockAccountAlert(
    onDismissRequest: () -> Unit, onConfirmation: () -> Unit, accountToMute: Account
) {
    AlertDialog(icon = {
        Icon(Icons.AutoMirrored.Outlined.VolumeOff, contentDescription = "Example Icon")
    }, title = {
        Text(text = "Unblock User?")
    }, text = {
        Text(text = accountToMute.acct)
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onConfirmation()
        }) {
            Text("Unblock")
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text("Cancel")
        }
    })
}