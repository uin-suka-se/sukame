package com.sukase.core.di

import android.content.Context
import androidx.room.Room
import com.sukase.core.data.source.database.SuKaMeDao
import com.sukase.core.data.source.database.SuKaMeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SuKaMeDatabase {
        return Room.databaseBuilder(
            context,
            SuKaMeDatabase::class.java,
            "SuKaMe.db"
        ).createFromAsset("database/dummy.db").build()
    }

    @Provides
    fun provideAnimeDao(database: SuKaMeDatabase): SuKaMeDao = database.suKaMeDao()
}