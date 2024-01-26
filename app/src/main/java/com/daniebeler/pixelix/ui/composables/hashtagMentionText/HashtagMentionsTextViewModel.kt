package com.daniebeler.pixelix.ui.composables.hashtagMentionText

import androidx.lifecycle.ViewModel
import com.daniebeler.pixelix.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HashtagMentionsTextViewModel@Inject constructor(
    private val repository: CountryRepository
): ViewModel()  {
    suspend fun getMyAccountId(): String {
        return repository.getAccountId().first()
    }
}