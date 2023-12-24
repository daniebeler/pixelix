package com.daniebeler.pixels.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiReplyElementDto(
    @SerializedName("data")
    val data: List<ReplyDto>
)
