package com.rebllelionandroid

import android.content.Context
import com.rebellionandroid.features.gamestateupdater.GameStateUpdater
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.GameTimerViewModel
import com.rebllelionandroid.core.di.modules.ContextModule
import com.rebllelionandroid.core.di.modules.DatabaseModule
import com.rebllelionandroid.ui.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {
    fun gameStateUpdater(): GameStateUpdater
    fun gameTimer(): GameTimerViewModel
    fun gameState(): GameStateViewModel
    fun inject(mainActivity: MainActivity)
    fun injectHome(homeFragment: HomeFragment)
    fun context(): Context
}
