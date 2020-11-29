package com.rebllelionandroid.core.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Class that contributes to the object graph [CoreComponent].
 *
 * @see Module
 */
@Module
class ContextModule(private val context: Context) {

    /**
     * Create a provider method binding for [Context].
     *
     * @return Instance of context.
     * @see Provides
     */
    @Provides
    fun provideContext(): Context = context
}
