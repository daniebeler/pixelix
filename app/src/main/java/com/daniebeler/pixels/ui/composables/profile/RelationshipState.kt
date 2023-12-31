package com.daniebeler.pixels.ui.composables.profile

import com.daniebeler.pixels.domain.model.Relationship

data class RelationshipState(
    val isLoading: Boolean = false,
    val accountRelationship: Relationship? = null,
    val error: String = ""
)
