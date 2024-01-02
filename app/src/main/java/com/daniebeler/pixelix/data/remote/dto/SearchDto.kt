package com.daniebeler.pixelix.data.remote.dto


import com.google.gson.annotations.SerializedName

data class SearchDto(
    @SerializedName("accounts")
    val accounts: List<AccountDto>,
    @SerializedName("statuses")
    val posts: List<PostDto>,
    @SerializedName("hashtags")
    val hashtags: List<TagDto>
)