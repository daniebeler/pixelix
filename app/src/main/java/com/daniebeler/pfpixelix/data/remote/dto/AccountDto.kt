package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.utils.HtmlToText.htmlToText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class AccountDto(
    @SerialName("acct") val acct: String?,
    @SerialName("avatar") val avatar: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("discoverable") val discoverable: Boolean?,
    @SerialName("display_name") val displayName: String?,
    @SerialName("followers_count") val followersCount: Int?,
    @SerialName("following_count") val followingCount: Int?,
    @SerialName("header_bg") val headerBg: JsonElement?,
    @SerialName("id") val id: String?,
    @SerialName("is_admin") val isAdmin: Boolean?,
    @SerialName("last_fetched_at") val lastFetchedAt: String?,
    @SerialName("local") val local: Boolean?,
    @SerialName("location") val location: JsonElement?,
    @SerialName("locked") val locked: Boolean?,
    @SerialName("note") val note: String?,
    @SerialName("note_text") val noteText: String?,
    @SerialName("pronouns") val pronouns: List<String>?,
    @SerialName("statuses_count") val statusesCount: Int?,
    @SerialName("url") val url: String?,
    @SerialName("username") val username: String?,
    @SerialName("website") val website: String?
) : DtoInterface<Account> {
    override fun toModel(): Account {
        return Account(
            id = id ?: "",
            username = username?: "",
            acct = acct ?: "",
            displayname = displayName ?: "",
            avatar = avatar ?: "",
            followersCount = followersCount ?: 0,
            followingCount = followingCount ?: 0,
            postsCount = statusesCount ?: 0,
            website = website ?: "",
            note =  htmlToText(note.orEmpty()),
            url = url ?: "",
            locked = locked ?: false,
            createdAt = createdAt ?: "",
            isAdmin = isAdmin ?: false,
            pronouns = pronouns ?: emptyList()
        )
    }
}