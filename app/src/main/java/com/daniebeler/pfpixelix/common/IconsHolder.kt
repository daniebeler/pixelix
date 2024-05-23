package com.daniebeler.pfpixelix.common

import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.settings.icon_selection.IconSelectionViewModel

object IconsHolder {
    val list = listOf(
        IconSelectionViewModel.IconAndName(
            "com.daniebeler.pfpixelix.MainActivity",
            R.mipmap.ic_launcher_02
        ),
        IconSelectionViewModel.IconAndName(
            "com.daniebeler.pfpixelix.Icon03",
            R.mipmap.ic_launcher_03
        ),
        IconSelectionViewModel.IconAndName(
            "com.daniebeler.pfpixelix.Icon01",
            R.mipmap.ic_launcher_01
        ),
        IconSelectionViewModel.IconAndName(
            "com.daniebeler.pfpixelix.Icon05",
            R.mipmap.ic_launcher_05
        ),
        IconSelectionViewModel.IconAndName(
            "com.daniebeler.pfpixelix.Icon06",
            R.mipmap.ic_launcher_06
        ),
        IconSelectionViewModel.IconAndName(
            "com.daniebeler.pfpixelix.Icon07",
            R.mipmap.ic_launcher_07
        ),
        IconSelectionViewModel.IconAndName("com.daniebeler.pfpixelix.Icon04", R.mipmap.ic_launcher)
    )
}