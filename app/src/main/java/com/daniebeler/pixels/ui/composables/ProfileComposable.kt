package com.daniebeler.pixels.ui.composables

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.ui.composables.trending.posts.CustomPost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileComposable(navController: NavController, userId: String, viewModel: ProfileViewModel = hiltViewModel()) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current


    viewModel.loadData(userId)


    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = viewModel.account?.username ?: "")
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
    ) {paddingValues ->
        if (viewModel.account != null) {
            Column (Modifier.padding(paddingValues)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    content = {

                        if (viewModel.account != null) {
                            item (
                                span = { GridItemSpan(3) }
                            ) {
                                ProfileTopSection(viewModel.account!!, navController)
                            }
                        }


                        item (
                            span = { GridItemSpan(3) }
                        ) {
                            Column (Modifier.padding(12.dp)) {
                                if (viewModel.relationship != null) {
                                    if (viewModel.relationship!!.following) {
                                        Button(onClick = {
                                            viewModel.unfollowAccount(userId)
                                        }) {
                                            Text(text = "unfollow")
                                        }
                                    }
                                    else {
                                        Button(onClick = {
                                            viewModel.followAccount(userId)
                                        }) {
                                            Text(text = "follow")
                                        }
                                    }
                                }

                                if (viewModel.mutalFollowers.isNotEmpty()) {
                                    Text(text = "MUTAL FOLLOWERS!!!", color = Color.Red)
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }

                        items(viewModel.posts, key = {
                            it.id
                        }) { photo ->
                            CustomPost(post = photo, navController = navController)
                        }

                        item {
                            Button(onClick = {
                                viewModel.loadMorePosts(userId)
                            }) {
                                Text(text = "Load More")
                            }
                        }

                    }
                )

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


                if (viewModel.relationship != null) {
                    if (viewModel.relationship!!.muting) {
                        CustomBottomSheetElement(icon = Icons.Outlined.DoNotDisturbOn, text = "Unmute this profile", onClick = {
                            viewModel.unmuteAccount()
                        })
                    }
                    else {
                        CustomBottomSheetElement(icon = Icons.Outlined.DoNotDisturbOn, text = "Mute this profile", onClick = {
                            viewModel.muteAccount()
                        })
                    }

                    if (viewModel.relationship!!.blocking) {
                        CustomBottomSheetElement(icon = Icons.Outlined.Block, text = "Unblock this profile", onClick = {
                            viewModel.unblockAccount()
                        })
                    }
                    else {
                        CustomBottomSheetElement(icon = Icons.Outlined.Block, text = "Block this profile", onClick = {
                            viewModel.blockAccount()
                        })
                    }
                }

                HorizontalDivider(Modifier.padding(12.dp))

                CustomBottomSheetElement(icon = Icons.Outlined.OpenInBrowser, text = "Open in browser", onClick = {
                    openUrl(context, viewModel.account!!.url)
                })

                CustomBottomSheetElement(icon = Icons.Outlined.Share, text = "Share this profile", onClick = {
                    shareProfile(context, viewModel.account!!.url)
                })
            }
        }
    }
}

private fun openUrl(context: Context, url: String){
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

    Column (Modifier.padding(12.dp)) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = account!!.avatar,
                contentDescription = "",
                modifier = Modifier
                    .height(76.dp)
                    .clip(CircleShape))

            Row (horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = account!!.postsCount.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Posts", fontSize = 12.sp)
                }

                Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                    navController.navigate("followers_screen/" + account.id) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }) {
                    Text(text = account!!.followersCount.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Followers", fontSize = 12.sp)
                }

                Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                    navController.navigate("followers_screen/" + account.id) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }) {
                    Text(text = account!!.followingCount.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Following", fontSize = 12.sp)
                }
            }



        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = account!!.displayname, fontWeight = FontWeight.Bold)

        account!!.note?.let {
            HashtagsMentionsTextView(text = account!!.note, onClick = {})
        }

        account!!.website?.let {
            Row (Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = account!!.website.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = { uriHandler.openUri(account!!.website.toString())})
                )
            }


        }


    }

}