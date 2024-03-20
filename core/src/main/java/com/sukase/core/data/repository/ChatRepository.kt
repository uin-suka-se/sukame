package com.sukase.core.data.repository

import android.database.SQLException
import android.util.Log
import androidx.datastore.core.DataStore
import com.sukase.core.R
import com.sukase.core.UserPreferences
import com.sukase.core.data.base.ApiException
import com.sukase.core.data.base.BaseError
import com.sukase.core.data.base.DataResource
import com.sukase.core.data.base.DatabaseException
import com.sukase.core.data.mapper.chatEntityToModel
import com.sukase.core.data.mapper.provideReceiverListModel
import com.sukase.core.data.mapper.provideSenderModel
import com.sukase.core.data.model.chat.entity.ChatEntity
import com.sukase.core.data.source.database.SuKaMeDao
import com.sukase.core.domain.base.DomainResource
import com.sukase.core.domain.model.ChatModel
import com.sukase.core.domain.usecase.chat.IChatRepository
import com.sukase.core.utils.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val dao: SuKaMeDao,
    private val dataStore: DataStore<UserPreferences>
) : IChatRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getChatList(
        token: String,
        conversationId: String
    ): Flow<DomainResource<List<ChatModel?>>> =
        dao.getChatList(conversationId.toInt()).flatMapConcat {
            flow {
                emit(DataResource.Loading.mapToDomainResource())
                emit(DataResource.Success(it.map {
                    if (it != null) {
                        chatEntityToModel(it)
                    } else {
                        null
                    }
                }).mapToDomainResource())
            }.catch {
                if (it.message.isNullOrBlank()) {
                    emit(
                        DataResource.Error(
                            BaseError(
                                UiText.StringResource(R.string.unknown_error)
                            ).mapToDomainThrowable()
                        ).mapToDomainResource()
                    )
                } else if (it is HttpException) {
                    emit(
                        DataResource.Error(
                            ApiException(
                                it.code().toString(),
                                it.message()
                            ).mapToDomainThrowable()
                        ).mapToDomainResource()
                    )
                } else if (it is SQLException) {
                    emit(
                        DataResource.Error(
                            DatabaseException(
                                UiText.DynamicString(it.message.toString())
                            ).mapToDomainThrowable()
                        ).mapToDomainResource()
                    )
                } else {
                    emit(
                        DataResource.Error(
                            BaseError(
                                UiText.DynamicString(it.message.toString())
                            ).mapToDomainThrowable()
                        ).mapToDomainResource()
                    )
                }
            }.flowOn(Dispatchers.IO)
        }

    override fun sendChat(
        token: String,
        conversationId: String,
        message: String
    ): Flow<DomainResource<ChatModel>> = flow {
        Log.d("chat", "masuk repo0")
        emit(DataResource.Loading.mapToDomainResource())
        Log.d("chat", "masuk repo1")
        Log.d("chat", "masuk repo2")
        val sender = dataStore.data.first()
        val receiver = dao.getConversation(conversationId.toInt()).first()
        var receiverId = setOf<Int>()
        var receiverUsername = setOf<String>()
        var receiverFullName = listOf<String>()
        receiver.participantsId.mapIndexed { index, i ->
            if (i.toString() != sender.uid) {
                receiverId = setOf(i)
                receiverUsername = setOf(receiver.participantsUsername.elementAt(index))
                receiverFullName = listOf(receiver.participantsFullName[index])
            }
        }
        dao.sendChat(
                ChatEntity(
                    id = 0,
                    conversationId = conversationId,
                    photo = "dummy.jpg",
                    type = "text",
                    message = message,
                    datetime = LocalDateTime.now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC).toEpochMilli(),
                    senderId = sender.uid.toInt(),
                    senderUsername = sender.username,
                    senderFullName = sender.fullName,
                    receiverId = receiverId,
                    receiverUsername = receiverUsername,
                    receiverFullName = receiverFullName
                )
        ).apply {
            val chat = ChatModel(
                id = this.toString(),
                photo = "dummy.jpg",
                type = "person",
                message = message,
                datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                sender = provideSenderModel(dataStore.data.first()),
                receiverList = provideReceiverListModel(
                    dao.getConversation(conversationId.toInt()).first()
                ),
            )
            Log.d("chat", "daonya bisa")
            emit(DataResource.Success(chat).mapToDomainResource())
        }
    }.catch {
        if (it.message.isNullOrBlank()) {
            emit(
                DataResource.Error(
                    BaseError(
                        UiText.StringResource(R.string.unknown_error)
                    ).mapToDomainThrowable()
                ).mapToDomainResource()
            )
        } else if (it is HttpException) {
            emit(
                DataResource.Error(
                    ApiException(
                        it.code().toString(),
                        it.message()
                    ).mapToDomainThrowable()
                ).mapToDomainResource()
            )
        } else if (it is SQLException) {
            emit(
                DataResource.Error(
                    DatabaseException(
                        UiText.DynamicString(it.message.toString())
                    ).mapToDomainThrowable()
                ).mapToDomainResource()
            )
        } else {
            emit(
                DataResource.Error(
                    BaseError(
                        UiText.DynamicString(it.message.toString())
                    ).mapToDomainThrowable()
                ).mapToDomainResource()
            )
        }
    }.flowOn(Dispatchers.IO)
}