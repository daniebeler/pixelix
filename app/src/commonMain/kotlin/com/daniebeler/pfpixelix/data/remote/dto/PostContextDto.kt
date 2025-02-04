package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.PostContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostContextDto(
    @SerialName("ancestors") val ancestors: List<PostDto>,
    @SerialName("descendants") val descendants: List<PostDto>,
) : DtoInterface<PostContext> {
    override fun toModel(): PostContext {
        return PostContext(ancestors = ancestors.map { post -> post.toModel() },
            descendants = descendants.map { post -> post.toModel() })
    }
}