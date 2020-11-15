package com.rebllelionandroid

import android.app.Application

class MainApplication: Application() {

    val appComponent = DaggerAppComponent
            .builder()
            .build()

    override fun onCreate() {
        super.onCreate()
        println("coreComponent")
    }
}