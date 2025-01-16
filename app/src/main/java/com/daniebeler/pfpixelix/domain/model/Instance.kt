package com.daniebeler.pfpixelix.domain.model

data class Instance(
    val domain: String,
    val rules: List<Rule>,
    val shortDescription: String,
    val description: String,
    val thumbnailUrl: String,
    val admin: Account?,
    val stats: InstanceStats,
    val version: String,
    val configuration: Configuration
)