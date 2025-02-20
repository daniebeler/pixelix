package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.StatusConfiguration
import kotlinx.serialization.Serializable

@Serializable
data class StatusConfigurationDto(
    val max_media_attachments: Int
): DtoInterface<StatusConfiguration> {
    override fun toModel(): StatusConfiguration {
        return StatusConfiguration(
            maxMediaAttachments = max_media_attachments
        )
    }
}