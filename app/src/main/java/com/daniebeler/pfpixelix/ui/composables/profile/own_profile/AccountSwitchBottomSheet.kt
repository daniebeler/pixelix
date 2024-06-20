package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.daniebeler.pfpixelix.domain.model.loginDataToAccount
import com.daniebeler.pfpixelix.gotoLoginActivity
import com.daniebeler.pfpixelix.ui.composables.custom_account.CustomAccount

@Composable
fun AccountSwitchBottomSheet(
    closeBottomSheet: () -> Unit,
    ownProfileViewModel: OwnProfileViewModel,
    viewModel: AccountSwitchViewModel = hiltViewModel(key = "account_switcher_viewmodel")
) {
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (viewModel.currentlyLoggedIn.currentAccount != null) {
            Text(
                text = "Current Account:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
            CustomAccount(account = loginDataToAccount(viewModel.currentlyLoggedIn.currentAccount!!))
        }
        if (viewModel.otherAccounts.otherAccounts.isNotEmpty()) {
            Text(
                text = "Other Accounts:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
            viewModel.otherAccounts.otherAccounts.map { otherAccount ->
                Box(Modifier.clickable {
                    viewModel.switchAccount(otherAccount) {
                        ownProfileViewModel.updateAccountSwitch()
                        closeBottomSheet()
                    }
                }) {
                    CustomAccount(account = loginDataToAccount(otherAccount))
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable { gotoLoginActivity(context) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.width(46.dp).height(46.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceContainer), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "add account",
                    Modifier
                        .height(32.dp)
                        .width(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Add Pixelfed account", lineHeight = 8.sp, fontWeight = FontWeight.Bold
            )
        }
    }
}