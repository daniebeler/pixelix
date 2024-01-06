package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.MediaAttachmentDto

data class MediaAttachment(
    val id: String,
    val url: String,
    val previewUrl: String,
    val meta: Meta?,
    val blurHash: String?
)

fun MediaAttachmentDto.toMediaAttachment(): MediaAttachment {
    return MediaAttachment(
        id = id,
        url = url,
        previewUrl = previewUrl,
        meta = meta?.toMeta(),
        blurHash = blurhash
    )
}