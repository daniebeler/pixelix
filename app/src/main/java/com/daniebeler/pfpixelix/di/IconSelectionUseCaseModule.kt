package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.usecase.DisableAllCustomAppIconsUseCase
import com.daniebeler.pfpixelix.domain.usecase.EnableCustomAppIconUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class IconSelectionUseCaseModule {

    @Provides
    @Singleton
    fun provideDisableAllCustomIconsUseCase(): DisableAllCustomAppIconsUseCase =
        DisableAllCustomAppIconsUseCase()

    @Provides
    @Singleton
    fun provideEnableCustomIconUseCase(): EnableCustomAppIconUseCase = EnableCustomAppIconUseCase()
}