package com.daniebeler.pfpixelix.domain.model
import androidx.compose.ui.graphics.vector.ImageVector

data class DomainSoftware(
    val name: String,
    val version: String,
    val icon: ImageVector?
)