package com.sukase.core.domain.usecase.chat

import com.sukase.core.domain.base.DomainResource
import com.sukase.core.domain.model.ChatModel
import kotlinx.coroutines.flow.Flow

interface IChatRepository {

    fun getChatList(
        token: String,
        conversationId: String
    ): Flow<DomainResource<List<ChatModel?>>>

    fun sendChat(
        token: String,
        conversationId: String,
        message: String
    ): Flow<DomainResource<ChatModel>>
}