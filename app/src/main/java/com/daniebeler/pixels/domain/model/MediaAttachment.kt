package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.MediaAttachmentDto

data class MediaAttachment(
    val id: String,
    val url: String,
    val previewUrl: String,
    val meta: Meta?
)

fun MediaAttachmentDto.toMediaAttachment(): MediaAttachment {
    return MediaAttachment(
        id = id,
        url = url,
        previewUrl = previewUrl,
        meta = meta?.toMeta()
    )
}