package com.aluengo.cleancomposerickandmorty.core.di

import com.aluengo.cleancomposerickandmorty.core.data.FakeRepository
import com.aluengo.cleancomposerickandmorty.core.data.Mapper
import com.aluengo.cleancomposerickandmorty.core.data.MockData
import com.aluengo.cleancomposerickandmorty.core.data.remote.ApiClient
import com.aluengo.cleancomposerickandmorty.core.data.remote.ApiService
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TestNetworkModule {

    @Singleton
    @Provides
    fun provideMapper() = Mapper()


    @Singleton
    @Provides
    fun provideRepository(mockData: MockData) =
        FakeRepository(mockData) as Repository

    @Singleton
    @Provides
    fun provideApiService(client: ApiClient) = ApiService(client)

    @Provides
    @Singleton
    fun provideClient(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }
}