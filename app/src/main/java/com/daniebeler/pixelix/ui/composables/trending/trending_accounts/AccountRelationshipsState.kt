package com.daniebeler.pixelix.ui.composables.trending.trending_accounts

import com.daniebeler.pixelix.domain.model.Relationship

data class AccountRelationshipsState(
    val isLoading: Boolean = false,
    val accountRelationships: List<Relationship> = emptyList(),
    val error: String = ""
)
