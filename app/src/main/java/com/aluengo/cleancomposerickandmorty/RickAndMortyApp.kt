package com.aluengo.cleancomposerickandmorty

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RickAndMortyApp : Application() {
    companion object {
        lateinit var instance: RickAndMortyApp
    }

    init {
        instance = this
    }


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}