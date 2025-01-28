package com.daniebeler.pfpixelix.ui.composables.settings.blocked_accounts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenLoadingComposable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BlockedAccountsComposable(
    navController: NavController,
    viewModel: BlockedAccountsViewModel = hiltViewModel(key = "blocked-accounts-key")
) {
    Scaffold(contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                text = stringResource(id = R.string.blocked_accounts), fontWeight = FontWeight.Bold
            )
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.chevron_back_outline), contentDescription = ""
                )
            }
        })

    }) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            PullToRefreshBox(
                isRefreshing = viewModel.blockedAccountsState.isRefreshing,
                onRefresh = { viewModel.getBlockedAccounts(true) },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
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
            }

            if (viewModel.blockedAccountsState.blockedAccounts.isEmpty()) {
                if (viewModel.blockedAccountsState.isLoading && !viewModel.blockedAccountsState.isRefreshing) {
                    FullscreenLoadingComposable()
                }

                if (viewModel.blockedAccountsState.error.isNotEmpty()) {
                    FullscreenErrorComposable(message = viewModel.blockedAccountsState.error)
                }

                if (!viewModel.blockedAccountsState.isLoading && viewModel.blockedAccountsState.error.isEmpty()) {
                    FullscreenEmptyStateComposable(EmptyState(heading = stringResource(id = R.string.no_blocked_accounts)))
                }
            }
        }
    }
}