package com.daniebeler.pfpixelix.domain.model

class MediaAttachmentConfiguration(
    val supportedMimeTypes: List<String>,
    val imageSizeLimit: Int,
    val videoSizeLimit: Int
)