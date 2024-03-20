package com.sukase.core.domain.usecase.chat

import com.sukase.core.domain.base.DomainResource
import com.sukase.core.domain.model.ChatModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatInteractor @Inject constructor(private val chatRepository: IChatRepository) :
    ChatUseCase {

    override fun getChatList(
        token: String,
        conversationId: String
    ): Flow<DomainResource<List<ChatModel?>>> {
        return chatRepository.getChatList(token, conversationId)
    }

    override fun sendChat(
        token: String,
        conversationId: String,
        message: String
    ): Flow<DomainResource<ChatModel>> {
        return chatRepository.sendChat(token, conversationId, message)
    }
}