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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.outlined.MoreVert
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
import androidx.compose.runtime.currentCompositionLocalContext
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixels.MainViewModel
import com.daniebeler.pixels.api.models.Account
import com.daniebeler.pixels.api.CountryRepository
import com.daniebeler.pixels.api.CountryRepositoryImpl
import com.daniebeler.pixels.api.models.Notification
import com.daniebeler.pixels.api.models.Post
import com.daniebeler.pixels.api.models.Relationship
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileComposable(viewModel: MainViewModel, navController: NavController, userId: String) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current



    var account: Account? by remember {
        mutableStateOf(null)
    }

    var relationships: List<Relationship> by remember {
        mutableStateOf(emptyList())
    }

    var mutalFollowers: List<Account> by remember {
        mutableStateOf(emptyList())
    }


    var posts: List<Post> by remember {
        mutableStateOf(emptyList())
    }

    val repository: CountryRepository = CountryRepositoryImpl()

    CoroutineScope(Dispatchers.Default).launch {
        viewModel.gotDataFromDataStore.collect { state ->
            if (state) {
                CoroutineScope(Dispatchers.Default).launch {
                    account = viewModel.returnAccount(userId)
                }

                CoroutineScope(Dispatchers.Default).launch {
                    relationships = viewModel.returnRelationships(userId)
                }

                CoroutineScope(Dispatchers.Default).launch {
                    mutalFollowers = viewModel.returnMutalFollowers(userId)
                }

                CoroutineScope(Dispatchers.Default).launch {
                    posts = repository.getPostsByAccountId(userId)
                }

            }
        }
    }

    fun loadMorePosts() {
        if (posts.isNotEmpty()) {
            var maxId = posts.last().id

            CoroutineScope(Dispatchers.Default).launch {
                posts = posts + repository.getPostsByAccountId(userId, maxId)
            }
        }
    }


    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = account?.username ?: "")
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
        if (account != null) {
            Column (Modifier.padding(paddingValues)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    content = {

                        if (account != null) {
                            item (
                                span = { GridItemSpan(3) }
                            ) {
                                ProfileTopSection(account!!)
                            }
                        }


                        item (
                            span = { GridItemSpan(3) }
                        ) {
                            Column {
                                if (relationships.isNotEmpty()) {
                                    if (relationships[0].following) {
                                        Button(onClick = { /*TODO*/ }) {
                                            Text(text = "unfollow")
                                        }
                                    }
                                    else {
                                        Button(onClick = { /*TODO*/ }) {
                                            Text(text = "follow")
                                        }
                                    }
                                }

                                if (mutalFollowers.isNotEmpty()) {
                                    Text(text = "MUTAL FOLLOWERS!!!", color = Color.Red)
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }

                        items(posts) { photo ->
                            CustomPost(post = photo, navController = navController)
                        }

                        item {
                            Button(onClick = {
                                loadMorePosts()
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
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.padding(bottom = 32.dp, start = 12.dp)
            ) {
                Text(text = "Open in browser", Modifier.clickable {
                    openUrl(context, account!!.url)
                })

                Text(text = "Share this profile", Modifier.clickable {
                    shareProfile(context, account!!.url)
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
fun ProfileTopSection(account: Account) {
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

                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = account!!.followersCount.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Followers", fontSize = 12.sp)
                }

                Column (horizontalAlignment = Alignment.CenterHorizontally) {
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
                    text = account!!.website,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = { uriHandler.openUri(account!!.website)})
                )
            }


        }


    }

}