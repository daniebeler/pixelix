package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Settings
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(
    private val repository: AccountRepository
) {
    operator fun invoke(): Flow<Resource<Settings>> {
        return repository.getAccountSettings()
    }
}