package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.usecase.AddNewLoginUseCase
import com.daniebeler.pfpixelix.domain.usecase.FinishLoginUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetCurrentLoginDataUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOngoingLoginUseCase
import com.daniebeler.pfpixelix.domain.usecase.ObtainTokenUseCase
import com.daniebeler.pfpixelix.domain.usecase.UpdateLoginDataUseCase
import com.daniebeler.pfpixelix.domain.usecase.VerifyTokenUseCase
import me.tatarka.inject.annotations.Component

@Component
abstract class EntryPointComponent(
    @Component val appComponent: AppComponent
) {
    abstract val obtainTokenUseCase: ObtainTokenUseCase
    abstract val verifyTokenUseCase: VerifyTokenUseCase
    abstract val updateLoginDataUseCase: UpdateLoginDataUseCase
    abstract val finishLoginUseCase: FinishLoginUseCase
    abstract val newLoginDataUseCase: AddNewLoginUseCase
    abstract val getOngoingLoginUseCase: GetOngoingLoginUseCase
    abstract val hostSelectionInterceptorInterface: HostSelectionInterceptorInterface
    abstract val currentLoginDataUseCase: GetCurrentLoginDataUseCase
    abstract val repository: CountryRepository
}