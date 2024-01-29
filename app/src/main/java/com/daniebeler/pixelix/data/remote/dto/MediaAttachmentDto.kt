package com.daniebeler.pixelix.data.remote.dto


import com.daniebeler.pixelix.domain.model.MediaAttachment
import com.google.gson.annotations.SerializedName

data class MediaAttachmentDto(
    @SerializedName("blurhash")
    val blurhash: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("filter_class")
    val filterClass: Any,
    @SerializedName("filter_name")
    val filterName: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_nsfw")
    val isNsfw: Int,
    @SerializedName("license")
    val license: LicenseDto,
    @SerializedName("meta")
    val meta: MetaDto?,
    @SerializedName("mime")
    val mime: String?,
    @SerializedName("optimized_url")
    val optimizedUrl: String,
    @SerializedName("orientation")
    val orientation: String,
    @SerializedName("preview_url")
    val previewUrl: String,
    @SerializedName("remote_url")
    val remoteUrl: Any,
    @SerializedName("text_url")
    val textUrl: Any,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String
) : DtoInterface<MediaAttachment> {
    override fun toModel(): MediaAttachment {
        return MediaAttachment(
            id = id,
            url = url,
            previewUrl = previewUrl,
            meta = meta?.toModel(),
            blurHash = blurhash,
            type = type,
            description = description
        )
    }
}