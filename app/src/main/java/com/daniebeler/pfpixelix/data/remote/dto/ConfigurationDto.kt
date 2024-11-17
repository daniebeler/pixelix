package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Configuration
import com.google.gson.annotations.SerializedName

class ConfigurationDto(
    @SerializedName("media_attachments") val mediaAttachmentsConfigurations: MediaAttachmentConfigurationDto
) : DtoInterface<Configuration> {
    override fun toModel(): Configuration {
        return Configuration(
            mediaAttachmentConfig = mediaAttachmentsConfigurations.toModel()
        )
    }
}
