package com.rebllelionandroid

import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.GameTimerViewModel
import com.rebllelionandroid.core.database.gamestate.GameState
import com.rebllelionandroid.core.di.modules.DatabaseModule
import com.rebllelionandroid.ui.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DatabaseModule::class, AppModule::class]
)
interface AppComponent {
    fun gameTimer(): GameTimerViewModel
    fun gameState(): GameStateViewModel
    fun inject(mainActivity: MainActivity)
    fun injectHome(homeFragment: HomeFragment)
}
