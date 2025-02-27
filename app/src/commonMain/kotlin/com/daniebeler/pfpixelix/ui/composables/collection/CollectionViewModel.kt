package com.daniebeler.pfpixelix.ui.composables.collection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.service.post.PostService
import com.daniebeler.pfpixelix.domain.usecase.AddPostOfCollectionUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetCollectionUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetPostsOfCollectionUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import com.daniebeler.pfpixelix.domain.usecase.RemovePostOfCollectionUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateCollectionUseCase
import com.daniebeler.pfpixelix.utils.KmpContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class CollectionViewModel @Inject constructor(
    private val getCollectionUseCase: GetCollectionUseCase,
    private val getPostsOfCollectionUseCase: GetPostsOfCollectionUseCase,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase,
    private val removePostOfCollectionUseCase: RemovePostOfCollectionUseCase,
    private val addPostOfCollectionUseCase: AddPostOfCollectionUseCase,
    private val updateCollectionUseCase: UpdateCollectionUseCase,
    private val postService: PostService
) : ViewModel() {

    var collectionState by mutableStateOf(CollectionState())
    var collectionPostsState by mutableStateOf(CollectionPostsState())
    var editState by mutableStateOf(EditCollectionState())

    fun loadData(collectionId: String) {
        if (collectionState.id == null) {
            collectionState = collectionState.copy(id = collectionId)
            getCollection()
            getPostsFirstLoad(false)
        }
    }

    private fun getCollection() {
        if (collectionState.id != null) {
            getCollectionUseCase(collectionState.id!!).onEach { result ->
                collectionState = when (result) {
                    is Resource.Success -> {
                        CollectionState(
                            collection = result.data, id = collectionState.id
                        )
                    }

                    is Resource.Error -> {
                        CollectionState(
                            error = result.message ?: "An unexpected error occurred",
                            id = collectionState.id
                        )
                    }

                    is Resource.Loading -> {
                        CollectionState(
                            isLoading = true,
                            collection = collectionState.collection,
                            id = collectionState.id
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }

    }

    private fun getPostsFirstLoad(refreshing: Boolean) {
        if (collectionState.id != null) {
            getPostsOfCollectionUseCase(collectionState.id!!).onEach { result ->
                collectionPostsState = when (result) {
                    is Resource.Success -> {
                        val endReached = (result.data?.size ?: 0) == 0
                        CollectionPostsState(
                            posts = result.data ?: emptyList(), endReached = endReached
                        )
                    }

                    is Resource.Error -> {
                        CollectionPostsState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }

                    is Resource.Loading -> {
                        CollectionPostsState(
                            isLoading = true,
                            isRefreshing = refreshing,
                            posts = collectionPostsState.posts
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getPostsExceptCollection() {
        postService.getOwnPosts().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val posts = result.data!!.filter {!editState.editPosts.contains(it)}
                    editState = editState.copy(allPostsExceptCollection = posts)
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {
                    editState = editState.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addPostToCollection(id: String) {
        val postToAdd = editState.allPostsExceptCollection.find { it.id == id }
        val allPosts = editState.allPostsExceptCollection.filter {it.id != id}
        postToAdd?.let {
            val posts = editState.editPosts + postToAdd
            editState = editState.copy(editPosts = posts, addedIds = editState.addedIds + id, allPostsExceptCollection = allPosts)
        }
    }

    fun confirmEdit() {
        collectionPostsState = collectionPostsState.copy(posts = editState.editPosts)
        editState = editState.copy(editMode = false)
        editState.removedIds.map {
            removePostOfCollection(it)
        }
        editState.addedIds.map {
            addPostsOfCollection(
                it
            )
        }
        if (editState.name != collectionState.collection!!.title) {
            updateCollection(editState.name)
        }
        collectionState = collectionState.copy(collection = collectionState.collection!!.copy(title = editState.name))
    }

    private fun updateCollection(newName: String) {
        if (collectionState.id != null && collectionState.collection != null) {
            updateCollectionUseCase(collectionState.id!!, newName, collectionState.collection!!.description, collectionState.collection!!.visibility).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        getCollection()
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun addPostsOfCollection(postId: String) {
        if (collectionState.id != null) {
            addPostOfCollectionUseCase(collectionState.id!!, postId).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        getPostsFirstLoad(false)
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun removePostOfCollection(postId: String) {
        if (collectionState.id != null) {
            removePostOfCollectionUseCase(collectionState.id!!, postId).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        getPostsFirstLoad(false)
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun toggleEditMode() {
        val newEditState = editState.copy()
        if (!editState.editMode) {
            newEditState.addedIds = emptyList()
            newEditState.removedIds = emptyList()
            newEditState.editPosts = collectionPostsState.posts
            newEditState.name = collectionState.collection?.title ?: ""
        }
        newEditState.editMode = !newEditState.editMode
        editState = newEditState
    }

    fun editRemove(id: String) {
        val newEditState = editState.copy()
        newEditState.removedIds += id
        newEditState.editPosts =
            newEditState.editPosts.filter { !newEditState.removedIds.contains(it.id) }
        editState = newEditState
    }

    fun refresh() {
        getPostsFirstLoad(true)
    }

    fun openUrl(url: String, context: KmpContext) {
        openExternalUrlUseCase(url, context)
    }
}