package com.daniebeler.pfpixelix.ui.composables.search

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.SavedSearches
import com.daniebeler.pfpixelix.domain.model.Type
import com.daniebeler.pfpixelix.domain.repository.SavedSearchesRepository
import com.daniebeler.pfpixelix.domain.usecase.SearchUseCase
import com.daniebeler.pfpixelix.utils.SavedSearchesSerializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val savedSearchesRepository: SavedSearchesRepository
) : ViewModel() {
    var textInput: String by mutableStateOf("")
    var searchState by mutableStateOf(SearchState())
    var savedSearches: SavedSearches by mutableStateOf(SavedSearches())

    init {
        viewModelScope.launch { getSavedSearches() }
    }

    private suspend fun getSavedSearches() {
        savedSearchesRepository.getSavedSearches().collect {
            savedSearches = it
        }
    }

    fun saveAccount(accountUsername: String, accountId: String, avatarUrl: String) {
        viewModelScope.launch {
            savedSearchesRepository.addAccount(accountUsername, accountId, avatarUrl);
        }
    }

    fun saveHashtag(accountId: String) {
        viewModelScope.launch {
            savedSearchesRepository.addHashtag(accountId);
        }
    }

    fun saveSearch(text: String) {
        if (text.isNotBlank()) {

            val savedSearchesBefore = savedSearches.pastSearches.filter { it.type == Type.Search }
            if (savedSearchesBefore.find { it.value == text } != null) {
                return
            }

            viewModelScope.launch {
                savedSearchesRepository.addSearch(text);
            }
        }
    }

    fun onSearch(text: String) {
        if (text.isNotBlank()) {
            textInputChange(text)
        }
    }

    fun textInputChange(text: String) {
        textInput = text
        searchDebounced(text)
    }

    private var searchJob: Job? = null

    private fun searchDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            if (searchText.isNotBlank()) {
                getSearchResults(searchText)
            }
        }
    }

    private fun getSearchResults(text: String) {
        searchUseCase(text).onEach { result ->
            searchState = when (result) {
                is Resource.Success -> {
                    SearchState(searchResult = result.data)
                }

                is Resource.Error -> {
                    SearchState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    SearchState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}