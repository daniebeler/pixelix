package com.daniebeler.pixelix.data.remote.dto

import com.daniebeler.pixelix.domain.model.Configuration
import com.daniebeler.pixelix.domain.model.MediaAttachmentConfiguration
import com.google.gson.annotations.SerializedName

class MediaAttachmentConfigurationDto(
    @SerializedName("supported_mime_types")
    val supportedMimeTypes: List<String>,
    @SerializedName("image_size_limit")
    val imageSizeLimit: Int,
    @SerializedName("video_size_limit")
    val videoSizeLimit: Int,
    ) : DtoInterface<MediaAttachmentConfiguration> {
    override fun toModel(): MediaAttachmentConfiguration {
        return MediaAttachmentConfiguration(
            supportedMimeTypes = supportedMimeTypes,
            imageSizeLimit = imageSizeLimit,
            videoSizeLimit = videoSizeLimit
        )
    }
}