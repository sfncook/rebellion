package com.rebellionandroid.features.sectorslist

import com.rebllelionandroid.core.di.GameStateComponent
import dagger.Component

@Component(dependencies = [GameStateComponent::class])
interface SectorsListComponent {
    @Component.Factory
    interface Factory {
        fun create(appComponent: GameStateComponent): SectorsListComponent
    }

    fun inject(activity: SectorsListActivity)
}