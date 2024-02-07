package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.domain.repository.CountryRepository

class Logout(
    private val repository: CountryRepository
) {
    operator fun invoke(accountId: String = "") {

    }
}