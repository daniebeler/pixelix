package com.daniebeler.pixels.data.remote.dto


import com.google.gson.annotations.SerializedName

data class MetaDto(
    @SerializedName("focus")
    val focus: FocusDto,
    @SerializedName("original")
    val original: OriginalDto?
)