package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.StoryUser
import com.google.gson.annotations.SerializedName

data class StoryUserDto(
    @SerializedName("avatar") val avatar: String,
    @SerializedName("id") val id: String,
    @SerializedName("is_author") val isAuthor: Boolean,
    @SerializedName("local") val local: Boolean,
    @SerializedName("username") val username: String,
    @SerializedName("username_acct") val usernameAcct: String?
) : DtoInterface<StoryUser> {
    override fun toModel(): StoryUser {
        return StoryUser(
            avatar = avatar,
            id = id,
            isAuthor = isAuthor,
            local = local,
            username = username
        )
    }
}