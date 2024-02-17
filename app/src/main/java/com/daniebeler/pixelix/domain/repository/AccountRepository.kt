package com.daniebeler.pixelix.domain.repository

import com.daniebeler.pixelix.data.remote.PixelfedApi
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val pixelfedApi: PixelfedApi
) {
}