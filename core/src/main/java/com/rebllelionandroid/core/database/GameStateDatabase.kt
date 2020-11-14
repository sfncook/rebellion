package com.rebllelionandroid.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rebllelionandroid.core.database.gamestate.GameState
import com.rebllelionandroid.core.database.gamestate.GameStateDao

@Database(
    entities = [GameState::class],
    exportSchema = false,
    version = 1
)
abstract class GameStateDatabase : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDao
}
