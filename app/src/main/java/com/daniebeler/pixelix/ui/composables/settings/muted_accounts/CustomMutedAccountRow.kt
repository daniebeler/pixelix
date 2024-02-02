package com.daniebeler.pixelix.ui.composables.settings.muted_accounts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.utils.Navigate

@Composable
fun CustomMutedAccountRow(
    account: Account,
    navController: NavController,
    viewModel: MutedAccountsViewModel
) {
    Row(
        Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                Navigate().navigate("profile_screen/" + account.id, navController)
            }
        ) {
            AsyncImage(
                model = account.avatar, contentDescription = "",
                modifier = Modifier
                    .height(32.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(text = account.username, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {
                viewModel.unmuteAlert = account.id
            }) {
                Text(text = stringResource(R.string.unmute))
            }
        }
    }
}