package com.daniebeler.pixels.ui.composables.trending.trending_accounts

import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Relationship

data class AccountRelationshipsState(
    val isLoading: Boolean = false,
    val accountRelationships: List<Relationship> = emptyList(),
    val error: String = ""
)
