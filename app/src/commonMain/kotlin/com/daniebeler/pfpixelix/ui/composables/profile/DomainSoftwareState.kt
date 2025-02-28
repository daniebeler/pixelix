package com.daniebeler.pfpixelix.ui.composables.profile

import com.daniebeler.pfpixelix.domain.model.FediServer
import com.daniebeler.pfpixelix.domain.model.FediSoftware

data class DomainSoftwareState(
    val isLoading: Boolean = false,
    val fediSoftware: FediSoftware? = null,
    val fediServer: FediServer? = null,
    val error: String = ""
)
