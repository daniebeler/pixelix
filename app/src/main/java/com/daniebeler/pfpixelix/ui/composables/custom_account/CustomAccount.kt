package com.daniebeler.pfpixelix.ui.composables.custom_account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.ui.composables.FollowButton
import com.daniebeler.pfpixelix.utils.Navigate
@Composable
fun CustomAccount(
    account: Account,
    relationship: Relationship?,
    navController: NavController,
    viewModel: CustomAccountViewModel = hiltViewModel(key = "custom-account" + account.id)
) {
    CustomAccountPrivate(
        account = account,
        relationship = relationship,
        navController = navController,
        onClick = {},
        viewModel = viewModel
    )
}

@Composable
fun CustomAccount(
    account: Account,
    relationship: Relationship?,
    onClick: () -> Unit,
    navController: NavController,
    viewModel: CustomAccountViewModel = hiltViewModel(key = "custom-account" + account.id)
) {
CustomAccountPrivate(
    account = account,
    relationship = relationship,
    onClick = onClick,
    navController = navController,
    viewModel = viewModel
)
}

@Composable
private fun CustomAccountPrivate(account: Account,
                          relationship: Relationship?,
                          onClick: () -> Unit,
                          navController: NavController,
                          viewModel: CustomAccountViewModel)
{
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
                Navigate.navigate("profile_screen/" + account.id, navController)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = account.avatar, contentDescription = "",
            modifier = Modifier
                .height(46.dp)
                .width(46.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = "@${account.username}")
            Text(
                text = "${account.followersCount} " + stringResource(id = R.string.followers),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        FollowButton(
            firstLoaded = relationship != null,
            isLoading = viewModel.relationshipState.isLoading,
            isFollowing = if (viewModel.gotUpdatedRelationship) viewModel.relationshipState.accountRelationship?.following
                ?: false else relationship?.following ?: false,
            onFollowClick = { viewModel.followAccount(account.id) },
            onUnFollowClick = { viewModel.unfollowAccount(account.id) }
        )
    }
}