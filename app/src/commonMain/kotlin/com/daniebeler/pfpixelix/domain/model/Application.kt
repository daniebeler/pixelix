package com.daniebeler.pfpixelix.domain.model

data class Application(
    val name: String,
    val id: String,
    val redirectUri: String,
    val clientId: String,
    val clientSecret: String
)