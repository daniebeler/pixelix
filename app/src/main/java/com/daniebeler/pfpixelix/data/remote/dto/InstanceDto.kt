package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Instance
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstanceDto(
    @SerialName("uri") val domain: String,
    @SerialName("rules") val rules: List<RuleDto>,
    @SerialName("short_description") val shortDescription: String,
    @SerialName("description") val description: String,
    @SerialName("thumbnail") val thumbnailUrl: String,
    @SerialName("contact_account") val admin: AccountDto?,
    @SerialName("stats") val stats: InstanceStatsDto,
    @SerialName("version") val version: String,
    @SerialName("configuration") val configuration: ConfigurationDto

) : DtoInterface<Instance> {
    override fun toModel(): Instance {
        return Instance(
            domain = domain,
            rules = rules.map { it.toModel() },
            shortDescription = shortDescription,
            thumbnailUrl = thumbnailUrl,
            description = description,
            admin = admin?.toModel() ?: null,
            stats = stats.toModel(),
            version = version,
            configuration = configuration.toModel()
        )
    }
}
