package com.daniebeler.pixelix.ui.composables.own_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.ui.composables.profile.AccountState
import com.daniebeler.pixelix.ui.composables.profile.PostsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnProfileViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var accountState by mutableStateOf(AccountState())
    var postsState by mutableStateOf(PostsState())
    private var accountId: String = ""

    init {
        loadData()
    }

    fun loadData() {
        CoroutineScope(Dispatchers.Default).launch {
            accountId = repository.getAccountId().first()

            getPosts(accountId)

            getAccount(accountId)
        }
    }

    private fun getAccount(userId: String) {
        repository.getAccount(userId).onEach { result ->
            accountState = when (result) {
                is Resource.Success -> {
                    AccountState(account = result.data)
                }

                is Resource.Error -> {
                    AccountState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    AccountState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getPosts(userId: String) {
        repository.getPostsByAccountId(userId).onEach { result ->
            postsState = when (result) {
                is Resource.Success -> {
                    PostsState(posts = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    PostsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    PostsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}