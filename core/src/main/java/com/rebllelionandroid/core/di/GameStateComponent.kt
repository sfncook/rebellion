package com.rebllelionandroid.core.di

import android.content.Context
import com.rebllelionandroid.core.GameStateUpdater
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.di.modules.ContextModule
import com.rebllelionandroid.core.di.modules.DatabaseModule
import dagger.Component

@Component(
    modules = [
        ContextModule::class,
        DatabaseModule::class
    ]
)
interface GameStateComponent {
    fun gameStateUpdater(): GameStateUpdater
    fun gameStateViewModel(): GameStateViewModel
    fun context(): Context
}
