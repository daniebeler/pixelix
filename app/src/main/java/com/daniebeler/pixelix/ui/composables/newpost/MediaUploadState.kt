package com.daniebeler.pixelix.ui.composables.newpost

import com.daniebeler.pixelix.domain.model.MediaAttachment

data class MediaUploadState(
    val isLoading: Boolean = false,
    val mediaAttachment: MediaAttachment? = null,
    val error: String = ""
)
