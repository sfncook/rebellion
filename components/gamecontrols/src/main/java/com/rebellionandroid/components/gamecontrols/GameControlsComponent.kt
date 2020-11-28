package com.rebellionandroid.components.gamecontrols

import com.rebllelionandroid.core.di.GameStateComponent
import dagger.Component

@Component(dependencies = [GameStateComponent::class])
interface GameControlsComponent {
    @Component.Factory
    interface Factory {
        fun create(appComponent: GameStateComponent): GameControlsComponent
    }

    fun inject(activity: GameControlsFragment)
}