package com.daniebeler.pixels.ui.composables

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
class MutedAccountsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {


    var mutedAccounts: List<Account> by mutableStateOf(emptyList())

    var unmuteAlert: String by mutableStateOf("")

    init {
        CoroutineScope(Dispatchers.Default).launch {
            mutedAccounts = repository.getMutedAccounts()
        }
    }

    fun unmuteAccount(accountId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            var res = repository.unmuteAccount(accountId)
            if (res != null) {
                mutedAccounts = repository.getMutedAccounts()
            }
        }
    }

}