package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.OriginalDto

data class Original(
    val aspect: Double
)

fun OriginalDto.toOriginal(): Original {
    return Original(
        aspect = aspect
    )
}