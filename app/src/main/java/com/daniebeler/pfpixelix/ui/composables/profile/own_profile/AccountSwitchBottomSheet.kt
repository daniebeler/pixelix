package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.gotoLoginActivity

@Composable
fun AccountSwitchBottomSheet(closeBottomSheet: () -> Unit, viewModel: AccountSwitchViewModel = hiltViewModel(key = "account_switcher_viewmodel")) {
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (viewModel.currentlyLoggedIn.currentAccount != null) {
            Row {
                AsyncImage(
                    model = viewModel.currentlyLoggedIn.currentAccount!!.avatar,
                    contentDescription = "",
                    modifier = Modifier
                        .height(46.dp)
                        .width(46.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = viewModel.currentlyLoggedIn.currentAccount!!.username)

            }
        }
        if (viewModel.otherAccounts.otherAccounts.isNotEmpty()) {
            viewModel.otherAccounts.otherAccounts.map { otherAccount ->
                Row(Modifier.clickable {
                    viewModel.switchAccount(otherAccount)
                }) {
                    AsyncImage(
                        model = otherAccount.avatar,
                        contentDescription = "",
                        modifier = Modifier
                            .height(46.dp)
                            .width(46.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = otherAccount.username)
                }
            }
        }

        IconButton(onClick = { gotoLoginActivity(context) }) {
            Icon(imageVector = Icons.Outlined.Add, contentDescription = "add account")
        }
    }
}