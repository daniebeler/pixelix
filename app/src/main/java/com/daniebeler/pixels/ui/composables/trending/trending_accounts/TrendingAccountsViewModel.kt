package com.daniebeler.pixels.ui.composables.trending.trending_accounts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.common.Resource
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.ui.composables.trending.trending_hashtags.TrendingHashtagsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TrendingAccountsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel(){
    var trendingAccountsState by mutableStateOf(TrendingAccountsState())
    var accountRelationShipsState by mutableStateOf(AccountRelationshipsState())
    init {
        getTrendingAccountsState()
    }

    private fun getTrendingAccountsState() {
        repository.getTrendingAccounts().onEach { result ->
            trendingAccountsState = when (result) {
                is Resource.Success -> {
                    result.data?.let { getRelationships(it) }
                    TrendingAccountsState(trendingAccounts = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    TrendingAccountsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    TrendingAccountsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getRelationships(accounts: List<Account>) {
        val accountIds: List<String> = accounts.map { account: Account -> account.id }

        repository.getRelationships(accountIds).onEach { result ->
            accountRelationShipsState = when (result) {
                is Resource.Success -> {
                    AccountRelationshipsState(accountRelationships = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    AccountRelationshipsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    AccountRelationshipsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}