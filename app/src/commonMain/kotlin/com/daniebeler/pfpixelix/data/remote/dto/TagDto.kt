package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Tag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    @SerialName("name") val name: String,
    @SerialName("hashtag") val hashtag: String?,
    @SerialName("url") val url: String,
    @SerialName("following") val following: Boolean?,
    @SerialName("count") val count: Int?,
    @SerialName("total") val total: Int?
) : DtoInterface<Tag> {
    override fun toModel(): Tag {
        return Tag(
            name = name,
            url = url,
            following = following ?: false,
            count = count,
            total = total ?: 0,
            hashtag = hashtag
        )
    }
}