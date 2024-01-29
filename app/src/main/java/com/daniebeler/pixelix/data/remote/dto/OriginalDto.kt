package com.daniebeler.pixelix.data.remote.dto


import com.daniebeler.pixelix.domain.model.Original
import com.google.gson.annotations.SerializedName

data class OriginalDto(
    @SerializedName("aspect")
    val aspect: Double,
    @SerializedName("height")
    val height: Int,
    @SerializedName("size")
    val size: String,
    @SerializedName("width")
    val width: Int
): DtoInterface<Original> {
    override fun toModel(): Original {
        return Original(
            aspect = aspect
        )
    }
}