package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MediaAttachmentDto(
    @SerialName("blurhash") val blurhash: String?,
    @SerialName("description") val description: String?,
    @SerialName("filter_class") val filterClass: JsonElement?,
    @SerialName("filter_name") val filterName: JsonElement?,
    @SerialName("id") val id: String,
    @SerialName("is_nsfw") val isNsfw: JsonElement?,
    @SerialName("license") val license: LicenseDto?,
    @SerialName("meta") val meta: MetaDto?,
    @SerialName("mime") val mime: String?,
    @SerialName("optimized_url") val optimizedUrl: String?,
    @SerialName("orientation") val orientation: String?,
    @SerialName("preview_url") val previewUrl: String,
    @SerialName("remote_url") val remoteUrl: JsonElement?,
    @SerialName("text_url") val textUrl: JsonElement?,
    @SerialName("type") val type: String,
    @SerialName("url") val url: String?
) : DtoInterface<MediaAttachment> {
    override fun toModel(): MediaAttachment {
        return MediaAttachment(
            id = id,
            url = url,
            previewUrl = previewUrl,
            meta = meta?.toModel(),
            blurHash = blurhash,
            type = type,
            description = description,
            license = license?.toModel()
        )
    }
}