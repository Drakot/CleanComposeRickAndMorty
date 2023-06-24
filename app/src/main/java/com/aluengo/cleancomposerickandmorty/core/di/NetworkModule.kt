package com.aluengo.cleancomposerickandmorty.core.di

import com.aluengo.cleancomposerickandmorty.BuildConfig
import com.aluengo.cleancomposerickandmorty.core.data.Mapper
import com.aluengo.cleancomposerickandmorty.core.data.RepositoryImpl
import com.aluengo.cleancomposerickandmorty.core.data.local.LocalStorage
import com.aluengo.cleancomposerickandmorty.core.data.remote.ApiClient
import com.aluengo.cleancomposerickandmorty.core.data.remote.ApiService
import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level =
                            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    }
                )
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideMapper() = Mapper()


    @Singleton
    @Provides
    fun provideRepository(service: ApiService, db: LocalStorage, mapper: Mapper) =
        RepositoryImpl(service, db, mapper) as Repository

    @Singleton
    @Provides
    fun provideApiService(client: ApiClient) = ApiService(client)

    @Provides
    @Singleton
    fun provideClient(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }
}