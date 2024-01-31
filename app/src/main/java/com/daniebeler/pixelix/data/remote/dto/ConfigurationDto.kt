package com.daniebeler.pixelix.data.remote.dto

import com.daniebeler.pixelix.domain.model.Configuration
import com.daniebeler.pixelix.domain.model.Instance
import com.google.gson.annotations.SerializedName

class ConfigurationDto(
    @SerializedName("media_attachments")
    val mediaAttachmentsConfigurations: MediaAttachmentConfigurationDto
) : DtoInterface<Configuration> {
    override fun toModel(): Configuration {
        return Configuration(
            mediaAttachmentConfig = mediaAttachmentsConfigurations.toModel()
        )
    }
}
