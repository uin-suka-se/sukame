package com.sukase.core.domain.usecase.conversation

import com.sukase.core.domain.base.DomainResource
import com.sukase.core.domain.model.ConversationModel
import com.sukase.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface ConversationUseCase {
    fun getAllConversationList(token: String): Flow<DomainResource<List<ConversationModel?>>>

    fun getUser(): Flow<DomainResource<UserModel?>>
}