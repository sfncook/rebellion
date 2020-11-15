package com.rebllelionandroid

import com.rebllelionandroid.ui.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}
