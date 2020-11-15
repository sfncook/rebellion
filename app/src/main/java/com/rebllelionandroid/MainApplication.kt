package com.rebllelionandroid

import android.app.Application
import android.content.Intent
import com.rebellionandroid.features.newgameactivity.NewGameActivity
import com.rebllelionandroid.core.GameTimerViewModel
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.GameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule

class MainApplication: Application() {

    lateinit var gameStateComponent: GameStateComponent

    override fun onCreate() {
        super.onCreate()
        initAppDependencyInjection()
    }

    private fun initAppDependencyInjection() {
        gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
    }
}