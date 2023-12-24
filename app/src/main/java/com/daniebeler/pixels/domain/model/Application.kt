package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.ApplicationDto

data class Application(
    val name: String,
    val id: String,
    val redirectUri: String,
    val clientId: String,
    val clientSecret: String
)

fun ApplicationDto.toApplication(): Application {
    return Application(
        name = name,
        id = id,
        redirectUri = redirectUri,
        clientId = clientId,
        clientSecret = clientSecret
    )
}