package com.sukase.core.data.repository

import android.database.SQLException
import androidx.datastore.core.DataStore
import com.sukase.core.R
import com.sukase.core.UserPreferences
import com.sukase.core.data.base.ApiException
import com.sukase.core.data.base.BaseError
import com.sukase.core.data.base.DataResource
import com.sukase.core.data.base.DatabaseException
import com.sukase.core.data.mapper.conversationMapper
import com.sukase.core.data.mapper.mapToUserModel
import com.sukase.core.data.source.database.SuKaMeDao
import com.sukase.core.domain.base.DomainResource
import com.sukase.core.domain.base.DomainThrowable
import com.sukase.core.domain.model.ConversationModel
import com.sukase.core.domain.model.UserModel
import com.sukase.core.domain.usecase.conversation.IConversationRepository
import com.sukase.core.utils.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepository @Inject constructor(
    private val dao: SuKaMeDao,
    private val dataStore: DataStore<UserPreferences>
) : IConversationRepository {

    override fun getUser(): Flow<DomainResource<UserModel?>> = flow {
        emit(DataResource.Loading.mapToDomainResource())
        val user = dataStore.data.firstOrNull()?.mapToUserModel()
        emit(DataResource.Success(user).mapToDomainResource())
    }.catch {
        if (it.message.isNullOrBlank()) {
            emit(
                DataResource.Error(
                    BaseError(
                        UiText.StringResource(R.string.unknown_error)
                    ).mapToDomainThrowable()
                ).mapToDomainResource()
            )
        } else if (it is IOException) {
            emit(
                DataResource.Error(
                    DomainThrowable("exception", UiText.DynamicString(it.message.toString()))
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllConversationList(
        token: String
    ): Flow<DomainResource<List<ConversationModel?>>> = dao.getConversationsList().flatMapConcat {
        flow {
            emit(DataResource.Loading.mapToDomainResource())
            emit(DataResource.Success(it.map
            {
                if (it != null) {
                    conversationMapper(it)
                } else {
                    null
                }
            }
            ).mapToDomainResource())
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
}