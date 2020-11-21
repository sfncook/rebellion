package com.rebllelionandroid.core.database.gamestate

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * The data access object for the [GameState] class.
 *
 * @see Dao
 */
@Dao
interface GameStateDao {

    @Query("SELECT * FROM game_state")
    fun getAllGameStatesLive(): LiveData<List<GameState>>

    @Query("SELECT * FROM game_state")
    fun getAllGameStates(): List<GameState>

    @Query("SELECT * FROM game_state LIMIT 1")
    fun getGameStateLive(): LiveData<GameState>

    @Query("SELECT * FROM game_state LIMIT 1")
    fun getGameState(): GameState

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createNewGameState(gameState: GameState)

    @Update
    fun updateGameState(gameState: GameState)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewSector(sector: Sector)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewPlanet(planet: Planet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewUnit(unit: Unit)
}
