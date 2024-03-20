package com.sukase.sukame.di

import com.sukase.core.domain.usecase.auth.AuthInteractor
import com.sukase.core.domain.usecase.auth.AuthUseCase
import com.sukase.core.domain.usecase.chat.ChatInteractor
import com.sukase.core.domain.usecase.chat.ChatUseCase
import com.sukase.core.domain.usecase.conversation.ConversationInteractor
import com.sukase.core.domain.usecase.conversation.ConversationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun provideAuthUseCase(authInteractor: AuthInteractor): AuthUseCase

    @Binds
    @Singleton
    abstract fun provideConversationUseCase(conversationInteractor: ConversationInteractor): ConversationUseCase

    @Binds
    @Singleton
    abstract fun provideChatUseCase(chatInteractor: ChatInteractor): ChatUseCase
}