package com.daniebeler.pfpixelix.ui.composables.profile.other_profile

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.BlockAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.FollowAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetDomainSoftwareUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetMutualFollowersUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetPostsOfAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetRelationshipsUseCase
import com.daniebeler.pfpixelix.domain.usecase.MuteAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnblockAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnfollowAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnmuteAccountUseCase
import com.daniebeler.pfpixelix.ui.composables.profile.AccountState
import com.daniebeler.pfpixelix.ui.composables.profile.DomainSoftwareState
import com.daniebeler.pfpixelix.ui.composables.profile.MutualFollowersState
import com.daniebeler.pfpixelix.ui.composables.profile.PostsState
import com.daniebeler.pfpixelix.ui.composables.profile.RelationshipState
import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OtherProfileViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val getPostsOfAccountUseCase: GetPostsOfAccountUseCase,
    private val followAccountUseCase: FollowAccountUseCase,
    private val unfollowAccountUseCase: UnfollowAccountUseCase,
    private val muteAccountUseCase: MuteAccountUseCase,
    private val unmuteAccountUseCase: UnmuteAccountUseCase,
    private val blockAccountUseCase: BlockAccountUseCase,
    private val unblockAccountUseCase: UnblockAccountUseCase,
    private val getMutualFollowersUseCase: GetMutualFollowersUseCase,
    private val getRelationshipsUseCase: GetRelationshipsUseCase,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase,
    private val getDomainSoftwareUseCase: GetDomainSoftwareUseCase,
    application: Application
    ) : AndroidViewModel(application) {

    var accountState by mutableStateOf(AccountState())
    var relationshipState by mutableStateOf(RelationshipState())
    var mutualFollowersState by mutableStateOf(MutualFollowersState())
    var postsState by mutableStateOf(PostsState())

    var domain by mutableStateOf("")
    var domainSoftwareState by mutableStateOf(DomainSoftwareState())
    var context = application
    var view by mutableStateOf(ViewEnum.Timeline)

    fun loadData(userId: String) {
        getAccount(userId)

        getPostsFirstLoad(userId)

        getRelationship(userId)

        getMutualFollowers(userId)
    }

    private fun getRelationship(userId: String) {
        getRelationshipsUseCase(List(1) { userId }).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    RelationshipState(
                        accountRelationship = if (!result.data.isNullOrEmpty()) {
                            result.data[0]
                        } else {
                            null
                        }
                    )
                }

                is Resource.Error -> {
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

    private fun getMutualFollowers(userId: String) {
        getMutualFollowersUseCase(userId).onEach { result ->
            mutualFollowersState = when (result) {
                is Resource.Success -> {
                    MutualFollowersState(mutualFollowers = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    MutualFollowersState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    MutualFollowersState(
                        isLoading = true, mutualFollowers = mutualFollowersState.mutualFollowers
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getAccount(userId: String) {
        getAccountUseCase(userId).onEach { result ->
            accountState = when (result) {
                is Resource.Success -> {
                    AccountState(account = result.data)
                }

                is Resource.Error -> {
                    AccountState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    AccountState(isLoading = true, account = accountState.account)
                }
            }

            if (accountState.account != null) {
                domain = accountState.account?.url?.substringAfter("https://")?.substringBefore("/")
                    ?: ""
                getDomainSoftware(domain)
            }
        }.launchIn(viewModelScope)
    }

    private fun getPostsFirstLoad(userId: String) {
        getPostsOfAccountUseCase(userId).onEach { result ->
            postsState = when (result) {
                is Resource.Success -> {
                    val endReached = (result.data?.size ?: 0) < Constants.PROFILE_POSTS_LIMIT
                    PostsState(posts = result.data ?: emptyList(), endReached = endReached)
                }

                is Resource.Error -> {
                    PostsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    PostsState(isLoading = true, posts = postsState.posts)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getPostsPaginated(userId: String) {
        if (postsState.posts.isNotEmpty() && !postsState.isLoading && !postsState.endReached) {
            getPostsOfAccountUseCase(userId, postsState.posts.last().id).onEach { result ->
                postsState = when (result) {
                    is Resource.Success -> {
                        val endReached = (result.data?.size ?: 0) < Constants.PROFILE_POSTS_LIMIT
                        PostsState(
                            posts = postsState.posts + (result.data ?: emptyList()),
                            endReached = endReached
                        )
                    }

                    is Resource.Error -> {
                        PostsState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        PostsState(isLoading = true, posts = postsState.posts)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun followAccount(userId: String) {
        followAccountUseCase(userId).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    RelationshipState(accountRelationship = result.data)
                }

                is Resource.Error -> {
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
        unfollowAccountUseCase(userId).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    RelationshipState(accountRelationship = result.data)
                }

                is Resource.Error -> {
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

    fun muteAccount(userId: String) {
        muteAccountUseCase(userId).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    RelationshipState(accountRelationship = result.data)
                }

                is Resource.Error -> {
                    RelationshipState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    RelationshipState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun unMuteAccount(userId: String) {
        unmuteAccountUseCase(userId).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    RelationshipState(accountRelationship = result.data)
                }

                is Resource.Error -> {
                    RelationshipState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    RelationshipState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun blockAccount(userId: String) {
        blockAccountUseCase(userId).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    RelationshipState(accountRelationship = result.data)
                }

                is Resource.Error -> {
                    RelationshipState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    RelationshipState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun unblockAccount(userId: String) {
        unblockAccountUseCase(userId).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    RelationshipState(accountRelationship = result.data)
                }

                is Resource.Error -> {
                    RelationshipState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    RelationshipState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getDomainSoftware(domain: String) {
        getDomainSoftwareUseCase(domain, context).onEach { result ->
            domainSoftwareState = when (result) {
                is Resource.Success -> {
                    DomainSoftwareState(domainSoftware = result.data)
                }

                is Resource.Error -> {
                    DomainSoftwareState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    DomainSoftwareState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun openUrl(context: Context, url: String) {
        openExternalUrlUseCase(context, url)
    }
}