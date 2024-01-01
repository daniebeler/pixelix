package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.ReplyDto
import com.daniebeler.pixels.data.remote.dto.SearchDto

data class Search(
    val accounts: List<Account>,
    val posts: List<Post>,
    val tags: List<Tag>
)

fun SearchDto.toSearch(): Search {
    return Search(
        accounts = accounts.map { accountDto -> accountDto.toAccount() },
        posts = posts.map { postDto -> postDto.toPost() },
        tags = hashtags.map { hashtagsDto -> hashtagsDto.toTag() }
    )
}