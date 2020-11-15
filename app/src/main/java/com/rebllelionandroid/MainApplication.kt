package com.rebllelionandroid

import android.app.Application
import com.rebllelionandroid.core.GameTimerViewModel
import com.rebllelionandroid.core.di.DaggerGameStateComponent
import com.rebllelionandroid.core.di.GameStateComponent
import com.rebllelionandroid.core.di.modules.ContextModule

class MainApplication: Application() {

    lateinit var gameStateComponent: GameStateComponent
    lateinit var gameTimerViewModel: GameTimerViewModel

    override fun onCreate() {
        super.onCreate()
        initAppDependencyInjection()
    }

    private fun initAppDependencyInjection() {
        gameStateComponent = DaggerGameStateComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()

        gameTimerViewModel = gameStateComponent.gameTimer()
    }
}