package com.sukase.core.di.feature

import com.sukase.core.data.repository.AuthRepository
import com.sukase.core.di.DataStoreModule
import com.sukase.core.di.DatabaseModule
import com.sukase.core.domain.usecase.auth.IAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DatabaseModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun provideRepository(authRepository: AuthRepository): IAuthRepository
}