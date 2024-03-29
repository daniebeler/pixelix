package com.daniebeler.pfpixelix.domain.model
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

data class DomainSoftware(
    val name: String,
    @DrawableRes
    val icon: Int,
    val link: String,
    val description: String
)