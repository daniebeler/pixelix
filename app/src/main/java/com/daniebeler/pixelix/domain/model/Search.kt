package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.SearchDto

data class Search(
    val accounts: List<Account>,
    val posts: List<Post>,
    val tags: List<Tag>
)