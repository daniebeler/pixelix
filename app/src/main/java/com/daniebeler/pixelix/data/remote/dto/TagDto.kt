package com.daniebeler.pixelix.data.remote.dto


import com.daniebeler.pixelix.domain.model.Tag
import com.google.gson.annotations.SerializedName

data class TagDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("following")
    val following: Boolean,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("total")
    val total: Int
) : DtoInterface<Tag> {
    override fun toModel(): Tag {
        return Tag(
            name = name,
            url = url,
            following = following,
            count = count,
            total = total
        )
    }
}