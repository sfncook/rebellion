package com.rebllelionandroid.core.di

import android.content.Context
import com.rebllelionandroid.core.GameStateViewModel
import com.rebllelionandroid.core.di.modules.ContextModule
import com.rebllelionandroid.core.di.modules.DatabaseModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextModule::class,
        DatabaseModule::class
    ]
)
interface GameStateComponent {
    fun gameStateViewModel(): GameStateViewModel
    fun context(): Context
}
