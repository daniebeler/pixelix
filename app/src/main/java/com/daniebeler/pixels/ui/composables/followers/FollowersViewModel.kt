package com.daniebeler.pixels.ui.composables.followers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var account: Account? by mutableStateOf(null)
    var followers: List<Account> by mutableStateOf(emptyList())
    var following: List<Account> by mutableStateOf(emptyList())

    fun loadAccount(accountId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            account = repository.getAccount(accountId)
        }
    }

    fun loadFollowers(accountId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            followers = repository.getAccountsFollowers(accountId)
        }
    }

    fun loadFollowing(accountId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            following = repository.getAccountsFollowing(accountId)
        }
    }
}