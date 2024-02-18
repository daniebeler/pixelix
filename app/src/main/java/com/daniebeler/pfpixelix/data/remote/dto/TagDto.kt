package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Tag
import com.google.gson.annotations.SerializedName

data class TagDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("hashtag")
    val hashtag: String?,
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
            total = total,
            hashtag = hashtag
        )
    }
}