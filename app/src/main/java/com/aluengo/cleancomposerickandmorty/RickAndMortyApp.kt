package com.aluengo.cleancomposerickandmorty

import android.app.Application
import timber.log.Timber


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