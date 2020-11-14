package com.rebllelionandroid.core.di.modules

import android.content.Context
import androidx.room.Room
import com.rebllelionandroid.core.database.GameStateDatabase
import com.rebllelionandroid.core.database.gamestate.GameStateDao
import com.rebllelionandroid.core.database.gamestate.GameStateRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideGameStatelDatabase(context: Context) =
        Room.databaseBuilder(
            context,
            GameStateDatabase::class.java,
            "gamestate-db"
        ).build()

    @Singleton
    @Provides
    fun provideGameStateDao(gameStateDatabase: GameStateDatabase) =
            gameStateDatabase.gameStateDao()

    @Singleton
    @Provides
    fun provideGameStateRepository(
            gameStateDao: GameStateDao
    ) = GameStateRepository(gameStateDao)

}
