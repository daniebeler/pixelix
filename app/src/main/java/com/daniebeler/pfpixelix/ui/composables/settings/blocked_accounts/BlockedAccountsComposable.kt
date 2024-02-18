package com.daniebeler.pfpixelix.ui.composables.settings.blocked_accounts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenLoadingComposable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BlockedAccountsComposable(
    navController: NavController, viewModel: BlockedAccountsViewModel = hiltViewModel()
) {

    val pullRefreshState =
        rememberPullRefreshState(refreshing = viewModel.blockedAccountsState.isRefreshing,
            onRefresh = { viewModel.getBlockedAccounts(true) })

    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Text(text = stringResource(id = R.string.blocked_accounts))
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ""
                )
            }
        })

    }) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                items(viewModel.blockedAccountsState.blockedAccounts, key = {
                    it.id
                }) {
                    Row {
                        CustomBlockedAccountRow(
                            account = it, navController = navController, viewModel
                        )
                    }
                }
            }

            if (viewModel.blockedAccountsState.blockedAccounts.isEmpty()) {
                if (viewModel.blockedAccountsState.isLoading && !viewModel.blockedAccountsState.isRefreshing) {
                    FullscreenLoadingComposable()
                }

                if (viewModel.blockedAccountsState.error.isNotEmpty()) {
                    FullscreenErrorComposable(message = viewModel.blockedAccountsState.error)
                }

                if (!viewModel.blockedAccountsState.isLoading && viewModel.blockedAccountsState.error.isEmpty()) {
                    FullscreenEmptyStateComposable {
                        Text(text = stringResource(id = R.string.no_blocked_accounts))
                    }
                }
            }

            CustomPullRefreshIndicator(
                viewModel.blockedAccountsState.isRefreshing,
                pullRefreshState,
            )
        }
    }

    if (viewModel.unblockAlert.isNotEmpty()) {
        AlertDialog(title = {
            Text(text = stringResource(R.string.unblock_account))
        }, text = {
            Text(text = stringResource(R.string.confirm_to_unblock_this_account))
        }, onDismissRequest = {
            viewModel.unblockAlert = ""
        }, confirmButton = {
            TextButton(onClick = {
                viewModel.unblockAccount(viewModel.unblockAlert)
                viewModel.unblockAlert = ""
            }) {
                Text(stringResource(R.string.unblock_caps))
            }
        }, dismissButton = {
            TextButton(onClick = {
                viewModel.unblockAlert = ""
            }) {
                Text(stringResource(R.string.cancel))
            }
        })
    }
}