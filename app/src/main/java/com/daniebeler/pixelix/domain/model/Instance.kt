package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.InstanceDto

data class Instance(
    val domain: String,
    val rules: List<Rule>
)

fun InstanceDto.toInstance(): Instance {
    return Instance(
        domain = domain,
        rules = rules.map { it.toRule() }
    )
}