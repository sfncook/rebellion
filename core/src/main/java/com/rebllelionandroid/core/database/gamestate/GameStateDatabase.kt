package com.rebllelionandroid.core.database.gamestate

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rebllelionandroid.core.database.Converters
import com.rebllelionandroid.core.database.staticTypes.PlanetType
import com.rebllelionandroid.core.database.staticTypes.SectorType
import com.rebllelionandroid.core.database.staticTypes.StaticTypesDao

@Database(
    entities = [
        GameState::class,
        Sector::class,
        Planet::class,
        Unit::class,
        Ship::class,
        Factory::class,
        DefenseStructure::class,
        SectorType::class,
        PlanetType::class
    ],
    exportSchema = false,
    version = 4
)
@TypeConverters(Converters::class)
abstract class GameStateDatabase : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDao
    abstract fun staticTypesDao(): StaticTypesDao
}
