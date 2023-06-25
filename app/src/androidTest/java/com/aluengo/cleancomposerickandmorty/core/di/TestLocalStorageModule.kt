package com.aluengo.cleancomposerickandmorty.core.di

import android.app.Application
import androidx.room.Room
import com.aluengo.cleancomposerickandmorty.core.data.local.Database
import com.aluengo.cleancomposerickandmorty.core.data.local.LocalStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TestLocalStorageModule {

    @Provides
    @Singleton
    fun provideInventoryLocalStorage(db: Database): LocalStorage {
        return LocalStorage(db.characterDao, db.infoDao)
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): Database {
        return Room.inMemoryDatabaseBuilder(
            app,
            Database::class.java,
        ).build()
    }
}