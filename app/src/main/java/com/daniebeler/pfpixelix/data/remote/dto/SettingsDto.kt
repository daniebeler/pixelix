package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Settings
import kotlinx.serialization.Serializable

@Serializable
data class SettingsDto(
    val advanced_atom: Boolean?,
    val disable_cw: Boolean?,
    val disable_embeds: Boolean?,
    val enable_reblogs: Boolean,
    val hide_collections: Boolean,
    val hide_groups: Boolean,
    val hide_like_counts: Boolean?,
    val hide_stories: Boolean,
    val mutual_mention_notifications: Boolean?,
    val photo_reblogs_only: Boolean?
): DtoInterface<Settings> {
    override fun toModel(): Settings {
        return Settings(
            hideGroups = hide_groups,
            enableReblogs = enable_reblogs,
            hideStories = hide_stories,
            hideCollections = hide_collections
        )
    }
}