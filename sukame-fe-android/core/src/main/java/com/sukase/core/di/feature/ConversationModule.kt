package com.sukase.core.di.feature

import com.sukase.core.data.repository.ConversationRepository
import com.sukase.core.di.DataStoreModule
import com.sukase.core.di.DatabaseModule
import com.sukase.core.domain.usecase.conversation.IConversationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [DatabaseModule::class, DataStoreModule::class])
abstract class ConversationModule {
    @Binds
    abstract fun provideRepository(conversationRepository: ConversationRepository): IConversationRepository
}