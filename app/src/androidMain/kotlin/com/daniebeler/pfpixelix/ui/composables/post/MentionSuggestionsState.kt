package com.daniebeler.pfpixelix.ui.composables.post

import com.daniebeler.pfpixelix.domain.model.Account

data class MentionSuggestionsState(
    val isLoading: Boolean = false,
    val mentions: List<Account> = emptyList(),
    val error: String = ""
)
