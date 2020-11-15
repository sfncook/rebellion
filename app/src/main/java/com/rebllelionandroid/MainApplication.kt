package com.rebllelionandroid

import android.app.Application
import com.rebllelionandroid.core.di.modules.ContextModule

class MainApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initAppDependencyInjection()
    }

    private fun initAppDependencyInjection() {
        appComponent = DaggerAppComponent
                .builder()
                .contextModule(ContextModule(this))
                .build()
    }
}