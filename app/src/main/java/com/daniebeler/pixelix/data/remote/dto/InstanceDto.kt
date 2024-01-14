package com.daniebeler.pixelix.data.remote.dto

import com.google.gson.annotations.SerializedName

data class InstanceDto(
    @SerializedName("domain")
    val domain: String,
    @SerializedName("rules")
    val rules: List<RuleDto>
)
