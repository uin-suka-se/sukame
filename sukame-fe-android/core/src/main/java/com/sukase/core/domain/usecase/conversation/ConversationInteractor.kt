package com.sukase.core.domain.usecase.conversation

import com.sukase.core.domain.base.DomainResource
import com.sukase.core.domain.model.ConversationModel
import com.sukase.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConversationInteractor @Inject constructor(private val conversationRepository: IConversationRepository) :
    ConversationUseCase {
    override fun getAllConversationList(
        token: String
    ): Flow<DomainResource<List<ConversationModel?>>> {
        return conversationRepository.getAllConversationList(token)
    }

    override fun getUser(): Flow<DomainResource<UserModel?>> {
        return conversationRepository.getUser()
    }
}