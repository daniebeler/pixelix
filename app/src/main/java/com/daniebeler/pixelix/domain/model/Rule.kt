package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.RuleDto

data class Rule(
    val id: String,
    val text: String
)

fun RuleDto.toRule(): Rule {
    return Rule(
        id = id,
        text = text
    )
}