package com.niemandkun.balloon

import android.app.Application
import timber.log.Timber

class BalloonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}