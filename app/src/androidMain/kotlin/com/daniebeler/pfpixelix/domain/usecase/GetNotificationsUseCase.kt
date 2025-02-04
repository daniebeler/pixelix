package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Notification
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetNotificationsUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(maxNotificationId: String = ""): Flow<Resource<List<Notification>>> {
        return repository.getNotifications(maxNotificationId)
    }
}