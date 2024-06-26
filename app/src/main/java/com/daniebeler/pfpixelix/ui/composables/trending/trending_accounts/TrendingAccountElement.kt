package com.daniebeler.pfpixelix.ui.composables.trending.trending_accounts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.ui.composables.CustomPost
import com.daniebeler.pfpixelix.ui.composables.custom_account.CustomAccount
import com.daniebeler.pfpixelix.ui.composables.hashtagMentionText.HashtagsMentionsTextView
import com.daniebeler.pfpixelix.utils.Navigate

@Composable
fun TrendingAccountElement(
    account: Account,
    relationship: Relationship?,
    navController: NavController,
    viewModel: TrendingAccountElementViewModel = hiltViewModel(key = account.id)
) {
    val context = LocalContext.current
    LaunchedEffect(account) {
        viewModel.loadItems(account.id)
    }

    Column(
        Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                Navigate.navigate("profile_screen/" + account.id, navController)
            }) {

        CustomAccount(account = account)

        if (account.note.isNotBlank()) {
            HashtagsMentionsTextView(
                text = account.note,
                mentions = null,
                navController = navController,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), openUrl = { url -> viewModel.openUrl(context, url) }
            )
        }

        NonlazyGrid(columns = 3, itemCount = minOf(9, viewModel.postsState.posts.size)) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .padding(horizontal = 2.dp)
            ) {
                CustomPost(post = viewModel.postsState.posts[it], navController = navController)
            }
        }
    }
}

@Composable
fun NonlazyGrid(
    columns: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    content: @Composable() (Int) -> Unit
) {
    Column(modifier = modifier) {
        var rows = (itemCount / columns)
        if (itemCount.mod(columns) > 0) {
            rows += 1
        }

        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns

            Row {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .weight(1f)
                    ) {
                        if (index < itemCount) {
                            content(index)
                        }
                    }
                }
            }
        }
    }
}
