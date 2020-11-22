package com.rebllelionandroid.core.database.gamestate

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rebllelionandroid.core.database.staticTypes.PlanetType
import com.rebllelionandroid.core.database.staticTypes.SectorType
import com.rebllelionandroid.core.database.staticTypes.StaticTypesDao
import com.rebllelionandroid.core.database.staticTypes.UnitType

@Database(
    entities = [
        GameState::class,
        Sector::class,
        Planet::class,
        Unit::class,
        SectorType::class,
        PlanetType::class,
        UnitType::class
    ],
    exportSchema = false,
    version = 4
)
abstract class GameStateDatabase : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDao
    abstract fun staticTypesDao(): StaticTypesDao
}
