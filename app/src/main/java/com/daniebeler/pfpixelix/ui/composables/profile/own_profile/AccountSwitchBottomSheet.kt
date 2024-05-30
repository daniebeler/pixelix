package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AccountSwitchBottomSheet(closeBottomSheet: () -> Unit, viewModel: AccountSwitchViewModel = hiltViewModel(key = "account_switcher_viewmodel")) {
    Column {
        if(viewModel.authData.loginDataList.isNotEmpty()) {
            viewModel.authData.loginDataList.map { 
                Text(text = it.accountId)
            }
        }
    }
}