package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Search
import com.google.gson.annotations.SerializedName

data class SearchDto(
    @SerializedName("accounts")
    val accounts: List<AccountDto>,
    @SerializedName("statuses")
    val posts: List<PostDto>,
    @SerializedName("hashtags")
    val hashtags: List<TagDto>
): DtoInterface<Search> {
    override fun toModel(): Search {
        return Search(
            accounts = accounts.map { accountDto -> accountDto.toModel() },
            posts = posts.map { postDto -> postDto.toModel() },
            tags = hashtags.map { hashtagsDto -> hashtagsDto.toModel() }
        )
    }
}