package com.sukase.core.data.repository

import android.database.SQLException
import android.database.sqlite.SQLiteConstraintException
import androidx.datastore.core.DataStore
import com.sukase.core.R
import com.sukase.core.UserPreferences
import com.sukase.core.data.base.ApiException
import com.sukase.core.data.base.BaseError
import com.sukase.core.data.base.DataResource
import com.sukase.core.data.base.DatabaseException
import com.sukase.core.data.model.register.entity.AccountEntity
import com.sukase.core.data.source.database.SuKaMeDao
import com.sukase.core.domain.base.DomainResource
import com.sukase.core.domain.usecase.auth.IAuthRepository
import com.sukase.core.utils.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val dao: SuKaMeDao,
    private val dataStore: DataStore<UserPreferences>
) : IAuthRepository {
    override suspend fun register(username: String, fullName: String): Flow<DomainResource<Boolean>> = flow {
        emit(DataResource.Loading.mapToDomainResource())
        dao.register(AccountEntity(username = username, fullName = fullName))
        emit(DataResource.Success(true).mapToDomainResource())
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
        } else if (it is SQLiteConstraintException) {
            emit(
                DataResource.Error(
                    DatabaseException(
                        UiText.StringResource(R.string.username_already_exist)
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


    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun login(username: String, fullName: String): Flow<DomainResource<Boolean>> = dao.getAccount(username, fullName).flatMapConcat { account ->
        flow {
            emit(DataResource.Loading.mapToDomainResource())
            if (account != null) {
                dataStore.updateData {
                    it.toBuilder().setUsername(username).setUid(account.id.toString())
                        .setFullName(fullName).setToken("user")
                        .build()
                }
                emit(DataResource.Success(true).mapToDomainResource())
            } else {
                emit(DataResource.Success(false).mapToDomainResource())
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
}