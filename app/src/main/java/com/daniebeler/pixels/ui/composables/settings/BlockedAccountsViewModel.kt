package com.daniebeler.pixels.ui.composables.settings

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
class BlockedAccountsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var blockedAccounts: List<Account> by mutableStateOf(emptyList())

    var unblockAlert: String by mutableStateOf("")

    init {
        CoroutineScope(Dispatchers.Default).launch {
            blockedAccounts = repository.getBlockedAccounts()
        }
    }

    fun unblockAccount(accountId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            var res = repository.unblockAccount(accountId)
            if (res != null) {
                blockedAccounts = repository.getBlockedAccounts()
            }
        }
    }
}