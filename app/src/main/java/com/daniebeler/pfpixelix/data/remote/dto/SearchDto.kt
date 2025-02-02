package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Search
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchDto(
    @SerialName("accounts") val accounts: List<AccountDto>,
    @SerialName("statuses") val posts: List<PostDto>,
    @SerialName("hashtags") val hashtags: List<TagDto>
) : DtoInterface<Search> {
    override fun toModel(): Search {
        return Search(accounts = accounts.map { accountDto -> accountDto.toModel() },
            posts = posts.map { postDto -> postDto.toModel() },
            tags = hashtags.map { hashtagsDto -> hashtagsDto.toModel() })
    }
}