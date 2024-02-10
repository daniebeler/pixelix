package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Notification
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationsUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(maxNotificationId: String = ""): Flow<Resource<List<Notification>>> {
        return repository.getNotifications(maxNotificationId)
    }
}