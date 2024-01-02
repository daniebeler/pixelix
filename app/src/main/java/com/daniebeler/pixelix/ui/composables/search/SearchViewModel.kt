package com.daniebeler.pixelix.ui.composables.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {
    var textInput: String by mutableStateOf("")
    var searchState by mutableStateOf(SearchState())
    fun textInputChange(text: String) {
        textInput = text
        getSearchResults(text)
    }

    private fun getSearchResults(text: String) {
        repository.search(text).onEach { result ->
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