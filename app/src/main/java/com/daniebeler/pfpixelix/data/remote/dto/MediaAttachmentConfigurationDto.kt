package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.MediaAttachmentConfiguration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MediaAttachmentConfigurationDto(
    @SerialName("supported_mime_types") val supportedMimeTypes: List<String>,
    @SerialName("image_size_limit") val imageSizeLimit: Int,
    @SerialName("video_size_limit") val videoSizeLimit: Int,
) : DtoInterface<MediaAttachmentConfiguration> {
    override fun toModel(): MediaAttachmentConfiguration {
        return MediaAttachmentConfiguration(
            supportedMimeTypes = supportedMimeTypes,
            imageSizeLimit = imageSizeLimit,
            videoSizeLimit = videoSizeLimit
        )
    }
}