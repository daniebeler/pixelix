package com.daniebeler.pixelix.ui.composables.profile

import com.daniebeler.pixelix.domain.model.Relationship

data class RelationshipState(
    val isLoading: Boolean = false,
    val accountRelationship: Relationship? = null,
    val error: String = ""
)
