package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Rule
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RuleDto(
    @SerialName("id") val id: String,
    @SerialName("text") val text: String
) : DtoInterface<Rule> {
    override fun toModel(): Rule {
        return Rule(
            id = id, text = text
        )
    }
}