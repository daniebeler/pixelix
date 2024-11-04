package com.daniebeler.pfpixelix.data.remote.dto

data class DomainSoftwareDto(
    val name: String, val version: String
) : DtoInterface<String> {
    override fun toModel(): String {
        return name
    }
}
