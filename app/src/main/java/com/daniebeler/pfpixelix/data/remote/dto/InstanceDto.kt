package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Instance
import com.google.gson.annotations.SerializedName

data class InstanceDto(
    @SerializedName("uri") val domain: String,
    @SerializedName("rules") val rules: List<RuleDto>,
    @SerializedName("short_description") val shortDescription: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnail") val thumbnailUrl: String,
    @SerializedName("contact_account") val admin: AccountDto,
    @SerializedName("stats") val stats: InstanceStatsDto,
    @SerializedName("version") val version: String,
    @SerializedName("configuration") val configuration: ConfigurationDto

) : DtoInterface<Instance> {
    override fun toModel(): Instance {
        return Instance(
            domain = domain,
            rules = rules.map { it.toModel() },
            shortDescription = shortDescription,
            thumbnailUrl = thumbnailUrl,
            description = description,
            admin = admin.toModel(),
            stats = stats.toModel(),
            version = version,
            configuration = configuration.toModel()
        )
    }
}
