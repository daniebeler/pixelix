package com.daniebeler.pfpixelix.ui.composables.profile

import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.DomainSoftware

data class DomainSoftwareState(
    val isLoading: Boolean = false,
    val domainSoftware: DomainSoftware? = null,
    val error: String = ""
)
