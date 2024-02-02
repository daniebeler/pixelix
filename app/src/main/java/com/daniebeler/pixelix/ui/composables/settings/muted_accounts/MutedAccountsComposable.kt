package com.daniebeler.pixelix.ui.composables.settings.muted_accounts

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
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pixelix.ui.composables.states.FullscreenLoadingComposable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MutedAccountsComposable(
    navController: NavController, viewModel: MutedAccountsViewModel = hiltViewModel()
) {
    val pullRefreshState =
        rememberPullRefreshState(refreshing = viewModel.mutedAccountsState.isRefreshing,
            onRefresh = { viewModel.getMutedAccounts(true) })

    Scaffold(topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Text(text = stringResource(id = R.string.muted_accounts))
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                items(viewModel.mutedAccountsState.mutedAccounts, key = {
                    it.id
                }) {
                    Row {
                        CustomMutedAccountRow(
                            account = it, navController = navController, viewModel
                        )
                    }
                }
            }

            if (viewModel.mutedAccountsState.mutedAccounts.isEmpty()) {
                if (viewModel.mutedAccountsState.isLoading && !viewModel.mutedAccountsState.isRefreshing) {
                    FullscreenLoadingComposable()
                }

                if (viewModel.mutedAccountsState.error.isNotEmpty()) {
                    FullscreenErrorComposable(message = viewModel.mutedAccountsState.error)
                }

                if (!viewModel.mutedAccountsState.isLoading && viewModel.mutedAccountsState.error.isEmpty()) {
                    FullscreenEmptyStateComposable()
                }
            }

            CustomPullRefreshIndicator(
                viewModel.mutedAccountsState.isRefreshing,
                pullRefreshState,
            )
        }

    }

    if (viewModel.unmuteAlert.isNotEmpty()) {
        AlertDialog(title = {
            Text(text = "Unmute Account")
        }, text = {
            Text(text = "Confirm to unmute this account")
        }, onDismissRequest = {
            viewModel.unmuteAlert = ""
        }, confirmButton = {
            TextButton(onClick = {
                viewModel.unmuteAccount(viewModel.unmuteAlert)
                viewModel.unmuteAlert = ""
            }) {
                Text(stringResource(R.string.unmute_caps))
            }
        }, dismissButton = {
            TextButton(onClick = {
                viewModel.unmuteAlert = ""
            }) {
                Text(stringResource(id = R.string.cancel))
            }
        })
    }
}