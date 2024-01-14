package com.daniebeler.pixelix.ui.composables.settings.about_instance

import com.daniebeler.pixelix.domain.model.Instance

data class InstanceState(
    val isLoading: Boolean = false,
    val instance: Instance? = null,
    val error: String = ""
)
