package com.daniebeler.pixelix.ui.composables.custom_account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.ui.composables.profile.RelationshipState
import com.daniebeler.pixelix.ui.composables.settings.muted_accounts.MutedAccountsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CustomAccountViewModel @Inject constructor(
    val repository: CountryRepository
): ViewModel() {

    var relationshipState by mutableStateOf(RelationshipState())
    var gotUpdatedRelationship by mutableStateOf(false)

    fun followAccount(userId: String) {
        repository.followAccount(userId).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    gotUpdatedRelationship = true
                    RelationshipState(accountRelationship = result.data)
                }

                is Resource.Error -> {
                    gotUpdatedRelationship = true
                    RelationshipState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    RelationshipState(
                        isLoading = true,
                        accountRelationship = relationshipState.accountRelationship
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun unfollowAccount(userId: String) {
        repository.unfollowAccount(userId).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    gotUpdatedRelationship = true
                    RelationshipState(accountRelationship = result.data)
                }

                is Resource.Error -> {
                    gotUpdatedRelationship = true
                    RelationshipState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    RelationshipState(
                        isLoading = true,
                        accountRelationship = relationshipState.accountRelationship
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}