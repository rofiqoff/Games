package com.rofiqoff.games.data.di

import com.rofiqoff.games.data.domain.repository.GameFavoriteRepository
import com.rofiqoff.games.data.domain.repository.GameRepository
import com.rofiqoff.games.data.implementation.repository.GameFavoriteRepositoryImpl
import com.rofiqoff.games.data.implementation.repository.GameRepositoryImpl
import com.rofiqoff.games.data.implementation.sources.local.database.AppDatabase
import com.rofiqoff.games.data.implementation.sources.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideGameRepository(
        apiService: ApiService,
    ): GameRepository {
        return GameRepositoryImpl(apiService)
    }

    @Singleton
    @Provides
    fun provideGameFavoriteRepository(
        database: AppDatabase,
    ): GameFavoriteRepository {
        return GameFavoriteRepositoryImpl(database)
    }

}
