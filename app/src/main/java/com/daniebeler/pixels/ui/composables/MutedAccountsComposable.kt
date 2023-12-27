package com.daniebeler.pixels.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Notification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MutedAccountsComposable (navController: NavController, viewModel: MutedAccountsViewModel = hiltViewModel()) {

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Muted accounts")
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
                }
            )

        }
    ) {paddingValues ->
        LazyColumn (Modifier.padding(paddingValues)) {
            items(viewModel.mutedAccounts, key = {
                it.id
            }) {
                Row {
                    CustomMutedAccountRow(account = it, navController = navController, viewModel)
                }
            }
        }
    }

    if (viewModel.unmuteAlert.isNotEmpty()) {
        AlertDialog(
            title = {
                Text(text = "Unmute Account")
            },
            text = {
                Text(text = "Confirm to unmute this account")
            },
            onDismissRequest = {
                viewModel.unmuteAlert = ""
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.unmuteAccount(viewModel.unmuteAlert)
                        viewModel.unmuteAlert = ""
                    }
                ) {
                    Text("Unmute")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.unmuteAlert = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CustomMutedAccountRow(account: Account, navController: NavController, viewModel: MutedAccountsViewModel) {
    Row (
        Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        Row (verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                navController.navigate("profile_screen/" + account.id) {
                    launchSingleTop = true
                    restoreState = true
                }
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
                Text(text = "unmute")
            }
        }

    }

}