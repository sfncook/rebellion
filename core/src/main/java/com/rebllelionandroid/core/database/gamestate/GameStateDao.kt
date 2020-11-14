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
    fun getAllGameStates(): LiveData<List<GameState>>

    @Insert
    fun createNewGameState(gameState: GameState)

    @Update
    fun updateGameState(gameState: GameState)
}
