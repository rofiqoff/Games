package com.rofiqoff.games.configuration.network

import com.rofiqoff.games.data.implementation.sources.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkProvider(): NetworkProvider {
        return NetworkProvider()
    }

    @Singleton
    @Provides
    fun provideRetrofit(networkProvider: NetworkProvider): Retrofit {
        return networkProvider.createRetrofit()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}
