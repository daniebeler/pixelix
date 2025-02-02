package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Configuration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ConfigurationDto(
    @SerialName("media_attachments") val mediaAttachmentsConfigurations: MediaAttachmentConfigurationDto
) : DtoInterface<Configuration> {
    override fun toModel(): Configuration {
        return Configuration(
            mediaAttachmentConfig = mediaAttachmentsConfigurations.toModel()
        )
    }
}
