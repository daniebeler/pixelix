package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.ApplicationDto

data class Application(
    val name: String,
    val id: String,
    val redirectUri: String,
    val clientId: String,
    val clientSecret: String
)