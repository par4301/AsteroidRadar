package com.udacity.asteroidradar

import android.app.Application
import timber.log.Timber

class AsteroidRadarApplication : Application() {
    companion object {
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        application = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}