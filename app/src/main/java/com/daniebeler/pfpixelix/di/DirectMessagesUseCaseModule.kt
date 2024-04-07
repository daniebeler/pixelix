package com.daniebeler.pfpixelix.di

import com.daniebeler.pfpixelix.domain.repository.DirectMessagesRepository
import com.daniebeler.pfpixelix.domain.usecase.DeleteMessageUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetChatUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetConversationsUseCase
import com.daniebeler.pfpixelix.domain.usecase.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DirectMessagesUseCaseModule {

    @Provides
    @Singleton
    fun provideGetConversationsUseCase(repository: DirectMessagesRepository): GetConversationsUseCase =
        GetConversationsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetChatUseCase(repository: DirectMessagesRepository): GetChatUseCase =
        GetChatUseCase(repository)

    @Provides
    @Singleton
    fun provideSendMessageUseCase(repository: DirectMessagesRepository): SendMessageUseCase =
        SendMessageUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteMessageUseCase(repository: DirectMessagesRepository): DeleteMessageUseCase =
        DeleteMessageUseCase(repository)
}