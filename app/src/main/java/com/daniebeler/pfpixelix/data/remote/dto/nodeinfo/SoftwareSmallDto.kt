package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.SoftwareSmall
import com.google.gson.annotations.SerializedName

data class SoftwareSmallDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("version")
    val version: String
): DtoInterface<SoftwareSmall> {
    override fun toModel(): SoftwareSmall {
        return SoftwareSmall(
            id = id,
            name = name,
            url = url,
            version = version
        )
    }
}