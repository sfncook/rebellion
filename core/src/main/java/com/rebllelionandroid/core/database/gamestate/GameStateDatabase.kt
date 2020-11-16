package com.rebllelionandroid.core.database.gamestate

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GameState::class],
    exportSchema = false,
    version = 4
)
abstract class GameStateDatabase : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDao
}
