package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
actual class UpdateAccountUseCase actual constructor(accountRepository: AccountRepository) {
    actual operator fun invoke(
        displayName: String,
        note: String,
        website: String,
        privateProfile: Boolean,
        avatarUri: KmpUri?,
        context: KmpContext
    ): Flow<Resource<Account>> {
        TODO("Not yet implemented")
    }
}