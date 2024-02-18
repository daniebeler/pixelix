package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Rule
import com.google.gson.annotations.SerializedName

data class RuleDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("text")
    val text: String
): DtoInterface<Rule> {
    override fun toModel(): Rule {
        return Rule(
            id = id,
            text = text
        )
    }
}