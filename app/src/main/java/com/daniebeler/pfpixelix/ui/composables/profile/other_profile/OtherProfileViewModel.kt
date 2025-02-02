package com.daniebeler.pfpixelix.ui.composables.profile.other_profile

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.BlockAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.FollowAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetAccountByUsernameUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetCollectionsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetMutualFollowersUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetPostsOfAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetRelationshipsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetViewUseCase
import com.daniebeler.pfpixelix.domain.usecase.MuteAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import com.daniebeler.pfpixelix.domain.usecase.SetViewUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnblockAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnfollowAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.UnmuteAccountUseCase
import com.daniebeler.pfpixelix.ui.composables.profile.AccountState
import com.daniebeler.pfpixelix.ui.composables.profile.CollectionsState
import com.daniebeler.pfpixelix.ui.composables.profile.MutualFollowersState
import com.daniebeler.pfpixelix.ui.composables.profile.PostsState
import com.daniebeler.pfpixelix.ui.composables.profile.RelationshipState
import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherProfileViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val getAccountByUsernameUseCase: GetAccountByUsernameUseCase,
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
    private val setViewUseCase: SetViewUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val getViewUseCase: GetViewUseCase,
    application: Application
) : AndroidViewModel(application) {
    var userId: String = ""
    var accountState by mutableStateOf(AccountState())
    var relationshipState by mutableStateOf(RelationshipState())
    var mutualFollowersState by mutableStateOf(MutualFollowersState())
    var postsState by mutableStateOf(PostsState())
    private var collectionPage by mutableIntStateOf(1)
    var collectionsState by mutableStateOf(CollectionsState())

    var domain by mutableStateOf("")
    var context = application
    var view by mutableStateOf(ViewEnum.Timeline)

    fun loadData(_userId: String, refreshing: Boolean) {
        userId = _userId
        getAccount(userId, refreshing)
        loadDataExceptAccount(refreshing)
    }

    private fun loadDataExceptAccount(refreshing: Boolean) {
        getPostsFirstLoad(userId, refreshing)

        getRelationship(userId)

        getMutualFollowers(userId)
        getCollections(userId, false)

        viewModelScope.launch {
            getViewUseCase().collect { res ->
                view = res
            }
        }
    }

    fun loadDataByUsername(username: String, refreshing: Boolean) {
        Log.d("byUsername", "load data by username")
        getAccountByUsername(username, refreshing)
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

    private fun getAccount(userId: String, refreshing: Boolean) {
        getAccountUseCase(userId).onEach { result ->
            accountState = when (result) {
                is Resource.Success -> {
                    AccountState(account = result.data)
                }

                is Resource.Error -> {
                    AccountState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    AccountState(isLoading = true, account = accountState.account, refreshing = refreshing)
                }
            }

            if (accountState.account != null) {
                domain = accountState.account?.url?.substringAfter("https://")?.substringBefore("/")
                    ?: ""
            }
        }.launchIn(viewModelScope)
    }

    private fun getAccountByUsername(username: String, refreshing: Boolean) {
        getAccountByUsernameUseCase(username).onEach { result ->
            accountState = when (result) {
                is Resource.Success -> {
                    userId = result.data!!.id
                    loadDataExceptAccount(refreshing)
                    AccountState(account = result.data)
                }

                is Resource.Error -> {
                    AccountState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    AccountState(isLoading = true, account = accountState.account, refreshing = refreshing)
                }
            }

            if (accountState.account != null) {
                domain = accountState.account?.url?.substringAfter("https://")?.substringBefore("/")
                    ?: ""
            }
        }.launchIn(viewModelScope)
    }

    fun getCollections(userId: String, paginated: Boolean) {
        if (collectionsState.endReached) {
            return
        }
        if (!paginated) {
            collectionPage = 1
        } else {
            collectionPage++
        }
        getCollectionsUseCase(userId, collectionPage).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    collectionsState = if (!paginated) {
                        CollectionsState(collections = result.data ?: emptyList())
                    } else {
                        val endReached = result.data!!.isEmpty()
                        CollectionsState(collections = collectionsState.collections + result.data, endReached = endReached)
                    }
                }

                is Resource.Error -> {
                    collectionsState = CollectionsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    collectionsState = CollectionsState(
                        isLoading = true, collections = collectionsState.collections
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getPostsFirstLoad(userId: String, refreshing: Boolean) {
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
                    PostsState(isLoading = true, posts = postsState.posts, refreshing = refreshing)
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

    fun openUrl(url: String, context: Context) {
        openExternalUrlUseCase(url, context)
    }

    fun changeView(newView: ViewEnum) {
        view = newView
        viewModelScope.launch {
            setViewUseCase(newView)
        }
    }

    fun postGetsDeleted(postId: String) {
        postsState = postsState.copy(posts = postsState.posts.filter { post -> post.id != postId })
    }
}