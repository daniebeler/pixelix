package com.daniebeler.pixelix.ui.composables.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.ui.composables.post.CustomBottomSheetElement
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.post.HashtagsMentionsTextView
import com.daniebeler.pixelix.ui.composables.LoadingComposable
import com.daniebeler.pixelix.ui.composables.trending.trending_posts.CustomPost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileComposable(
    navController: NavController,
    userId: String,
    viewModel: ProfileViewModel = hiltViewModel(key = userId)
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
        Box {
            if (viewModel.accountState.account != null) {
                Column(Modifier.padding(paddingValues)) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        content = {

                            item(
                                span = { GridItemSpan(3) }
                            ) {
                                ProfileTopSection(viewModel.accountState.account!!, navController)
                            }


                            item(
                                span = { GridItemSpan(3) }
                            ) {
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

                            items(viewModel.postsState.posts, key = {
                                it.id
                            }) { photo ->
                                CustomPost(post = photo, navController = navController)
                            }

                            if (!viewModel.postsState.isLoading && viewModel.postsState.posts.isEmpty()) {
                                item(span = { GridItemSpan(3) }) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.aspectRatio(2f)
                                    ) {
                                        Text(text = "No posts")
                                    }
                                }
                            }

                            if (!viewModel.postsState.isLoading && viewModel.postsState.posts.isNotEmpty()) {
                                item {
                                    Button(onClick = {
                                        //viewModel.loadMorePosts(userId)
                                    }) {
                                        Text(text = stringResource(R.string.load_more))
                                    }
                                }
                            }
                        }
                    )

                }
            }

            LoadingComposable(isLoading = viewModel.accountState.isLoading)
            ErrorComposable(message = viewModel.accountState.error)
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
                                viewModel.unmuteAccount(userId)
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
                    openUrl(context, viewModel.accountState.account!!.url)
                })

                CustomBottomSheetElement(
                    icon = Icons.Outlined.Share,
                    text = stringResource(R.string.share_this_profile),
                    onClick = {
                        shareProfile(context, viewModel.accountState.account!!.url)
                    })
            }
        }
    }
}

private fun openUrl(context: Context, url: String) {
    val intent = CustomTabsIntent.Builder().build()
    intent.launchUrl(context, Uri.parse(url))
}

private fun shareProfile(context: Context, url: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Composable
fun ProfileTopSection(account: Account, navController: NavController) {
    val uriHandler = LocalUriHandler.current

    Column(Modifier.padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = account!!.avatar,
                contentDescription = "",
                modifier = Modifier
                    .height(76.dp)
                    .clip(CircleShape)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = account!!.postsCount.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(text = stringResource(R.string.posts), fontSize = 12.sp)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        navController.navigate("followers_screen/" + "followers/" + account.id) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                    Text(
                        text = account!!.followersCount.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(text = stringResource(R.string.followers), fontSize = 12.sp)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        navController.navigate("followers_screen/" + "following/" + account.id) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                    Text(
                        text = account!!.followingCount.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(text = stringResource(R.string.following), fontSize = 12.sp)
                }
            }


        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = account!!.displayname, fontWeight = FontWeight.Bold)
        Text(text = "@" + account!!.acct, fontSize = 12.sp)

        account!!.note?.let {
            HashtagsMentionsTextView(text = account!!.note, onClick = {})
        }

        account!!.website?.let {
            Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = account!!.website.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = { uriHandler.openUri(account!!.website.toString()) })
                )
            }


        }


    }

}