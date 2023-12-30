package com.daniebeler.pixels.ui.composables.settings.muted_accounts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.common.Resource
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.ui.composables.settings.blocked_accounts.BlockedAccountsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MutedAccountsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {


    var mutedAccountsState by mutableStateOf(MutedAccountsState())

    var unmuteAlert: String by mutableStateOf("")

    init {
        getMutedAccounts()
    }

    private fun getMutedAccounts() {
        repository.getMutedAccounts().onEach { result ->
            mutedAccountsState = when (result) {
                is Resource.Success -> {
                    MutedAccountsState(mutedAccounts = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    MutedAccountsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    MutedAccountsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun unmuteAccount(accountId: String) {
        viewModelScope.launch {
            var res = repository.unmuteAccount(accountId)
            if (res != null) {
                getMutedAccounts()
            }
        }
    }

}