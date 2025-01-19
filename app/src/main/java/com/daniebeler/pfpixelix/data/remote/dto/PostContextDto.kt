package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.PostContext
import com.google.gson.annotations.SerializedName

data class PostContextDto(
    @SerializedName("ancestors") val ancestors: List<PostDto>,
    @SerializedName("descendants") val descendants: List<PostDto>,
) : DtoInterface<PostContext> {
    override fun toModel(): PostContext {
        return PostContext(ancestors = ancestors.map { post -> post.toModel() },
            descendants = descendants.map { post -> post.toModel() })
    }
}