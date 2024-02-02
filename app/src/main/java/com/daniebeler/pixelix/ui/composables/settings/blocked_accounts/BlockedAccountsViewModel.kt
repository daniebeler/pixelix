package com.daniebeler.pixelix.ui.composables.settings.blocked_accounts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BlockedAccountsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var blockedAccountsState by mutableStateOf(BlockedAccountsState())

    var unblockAlert: String by mutableStateOf("")

    init {
        getBlockedAccounts()
    }

    fun getBlockedAccounts(refreshing: Boolean = false) {
        repository.getBlockedAccounts().onEach { result ->
            blockedAccountsState = when (result) {
                is Resource.Success -> {
                    BlockedAccountsState(blockedAccounts = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    BlockedAccountsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    BlockedAccountsState(isLoading = true, isRefreshing = refreshing, blockedAccounts = blockedAccountsState.blockedAccounts)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun unblockAccount(accountId: String) {
        repository.unblockAccount(accountId).onEach { result ->
            blockedAccountsState = when (result) {
                is Resource.Success -> {
                    val newBlockedAccounts = blockedAccountsState.blockedAccounts.filter { account: Account -> account.id != accountId }
                    BlockedAccountsState(blockedAccounts = newBlockedAccounts)
                }

                is Resource.Error -> {
                    BlockedAccountsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    BlockedAccountsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}