package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.repository.AuthRepository
import me.tatarka.inject.annotations.Inject

@Inject
class GetCurrentLoginDataUseCase constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(): LoginData? {
        return repository.getCurrentLoginData()
    }
}