package com.daniebeler.pfpixelix.domain.usecase

import androidx.compose.ui.graphics.ImageBitmap
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.getAppIcons
import me.tatarka.inject.annotations.Inject

@Inject
class GetActiveAppIconUseCase {
    operator fun invoke(context: KmpContext): ImageBitmap? =
        context.getAppIcons().firstOrNull { it.enabled }?.icon
}