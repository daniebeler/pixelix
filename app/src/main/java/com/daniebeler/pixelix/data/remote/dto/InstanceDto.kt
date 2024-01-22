package com.daniebeler.pixelix.data.remote.dto

import com.daniebeler.pixelix.domain.model.Instance
import com.daniebeler.pixelix.domain.model.toRule
import com.google.gson.annotations.SerializedName

data class InstanceDto(
    @SerializedName("domain")
    val domain: String,
    @SerializedName("rules")
    val rules: List<RuleDto>
): DtoInterface<Instance> {
    override fun toModel(): Instance {
        return Instance(
            domain = domain,
            rules = rules.map { it.toRule() }
        )
    }
}
