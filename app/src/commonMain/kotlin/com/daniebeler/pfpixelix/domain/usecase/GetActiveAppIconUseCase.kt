package com.daniebeler.pfpixelix.domain.usecase

import androidx.compose.ui.graphics.ImageBitmap
import com.daniebeler.pfpixelix.utils.KmpContext
import me.tatarka.inject.annotations.Inject

@Inject
expect class GetActiveAppIconUseCase {
    operator fun invoke(context: KmpContext): ImageBitmap?
}