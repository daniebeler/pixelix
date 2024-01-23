package com.daniebeler.pixelix.domain.model

data class Instance(
    val domain: String,
    val rules: List<Rule>,
    val shortDescription: String,
    val description: String,
    val thumbnailUrl: String
)