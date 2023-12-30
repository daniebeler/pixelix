package com.daniebeler.pixels.ui.composables.settings.blocked_accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixels.domain.model.Account

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedAccountsComposable (navController: NavController, viewModel: BlockedAccountsViewModel = hiltViewModel()) {

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Blocked accounts")
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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            viewModel.blockedAccounts.blockedAccounts?.let {blockedAccounts ->
                LazyColumn {
                    items(blockedAccounts, key = {
                        it.id
                    }) {
                        Row {
                            CustomBlockedAccountRow(account = it, navController = navController, viewModel)
                        }
                    }
                }
            }

            if (viewModel.blockedAccounts.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            
            if (viewModel.blockedAccounts.error.isNotBlank()) {
                Text(text = viewModel.blockedAccounts.error, Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
            }
        }
    }

    if (viewModel.unblockAlert.isNotEmpty()) {
        AlertDialog(
            title = {
                Text(text = "Unblock Account")
            },
            text = {
                Text(text = "Confirm to unblock this account")
            },
            onDismissRequest = {
                viewModel.unblockAlert = ""
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.unblockAccount(viewModel.unblockAlert)
                        viewModel.unblockAlert = ""
                    }
                ) {
                    Text("Unblock")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.unblockAlert = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CustomBlockedAccountRow(account: Account, navController: NavController, viewModel: BlockedAccountsViewModel) {
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
                viewModel.unblockAlert = account.id
            }) {
                Text(text = "unblock")
            }
        }

    }

}