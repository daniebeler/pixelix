package com.daniebeler.pfpixelix.ui.composables.hashtagMentionText

import androidx.lifecycle.ViewModel
import com.daniebeler.pfpixelix.domain.usecase.GetCurrentLoginDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextWithClickableHashtagsAndMentionsViewModel @Inject constructor(
    private val currentLoginDataUseCase: GetCurrentLoginDataUseCase
) : ViewModel() {
    suspend fun getMyAccountId(): String {
        return currentLoginDataUseCase()!!.accountId
    }
}