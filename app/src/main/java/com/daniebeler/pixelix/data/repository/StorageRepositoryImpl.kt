package com.daniebeler.pixelix.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.daniebeler.pixelix.domain.repository.StorageRepository
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: DataStore<Preferences>
) : StorageRepository {


}