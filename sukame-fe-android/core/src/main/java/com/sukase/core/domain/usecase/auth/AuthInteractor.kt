package com.sukase.core.domain.usecase.auth

import com.sukase.core.domain.base.DomainResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: IAuthRepository) :
    AuthUseCase {
    override suspend fun register(username: String, fullName: String): Flow<DomainResource<Boolean>> {
        return authRepository.register(username, fullName)
    }

    override suspend fun login(username: String, fullName: String): Flow<DomainResource<Boolean>> {
        return authRepository.login(username, fullName)
    }

}