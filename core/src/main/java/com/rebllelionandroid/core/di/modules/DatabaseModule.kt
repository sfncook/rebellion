package com.rebllelionandroid.core.di.modules

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rebllelionandroid.core.database.gamestate.GameStateDao
import com.rebllelionandroid.core.database.gamestate.GameStateDatabase
import com.rebllelionandroid.core.database.gamestate.GameStateRepository
import com.rebllelionandroid.core.database.migrations.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabaseModule {

    @Provides
    fun provideGameStatelDatabase(context: Context) =
        Room.databaseBuilder(
                context,
                GameStateDatabase::class.java,
                "gamestate-db"
        ).build()

    @Provides
    fun provideGameStateDao(gameStateDatabase: GameStateDatabase) =
            gameStateDatabase.gameStateDao()

    @Provides
    fun provideGameStateRepository(
            gameStateDao: GameStateDao
    ) = GameStateRepository(gameStateDao)

}
