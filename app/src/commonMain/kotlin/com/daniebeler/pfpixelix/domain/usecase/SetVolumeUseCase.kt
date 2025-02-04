package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
class SetVolumeUseCase(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(volume: Boolean) {
        repository.storeVolume(volume)
    }
}