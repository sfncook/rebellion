package com.rebllelionandroid

import com.rebllelionandroid.core.di.modules.DatabaseModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DatabaseModule::class, AppModule::class]
)
interface AppComponent {
    fun inject(application: MainApplication)
}
