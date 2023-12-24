package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.OriginalDto

data class Original(
    val aspect: Double
)

fun OriginalDto.toOriginal(): Original {
    return Original(
        aspect = aspect
    )
}