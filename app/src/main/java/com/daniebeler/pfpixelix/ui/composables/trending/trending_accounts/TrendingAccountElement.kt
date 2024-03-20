package com.daniebeler.pfpixelix.ui.composables.trending.trending_accounts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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

        CustomAccount(account = account, relationship = relationship, navController = navController)

        if (account.note.isNotBlank()) {
            HashtagsMentionsTextView(
                text = account.note,
                mentions = null,
                navController = navController,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        LazyRow(contentPadding = PaddingValues(horizontal = 10.dp)) {
            items(viewModel.postsState.posts, key = {
                it.id
            }) { item ->
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .padding(horizontal = 2.dp)
                ) {
                    CustomPost(post = item, navController = navController)
                }
            }

            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(150.dp)
                        .padding(horizontal = 18.dp)
                ) {
                    Button(onClick = {
                        Navigate.navigate("profile_screen/" + account.id, navController)
                    }) {
                        Text(text = stringResource(R.string.view_more))
                    }
                }
            }
        }
    }
}