package com.rebllelionandroid.core.database.gamestate

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GameStateRepository @Inject constructor(
    private val gameStateDao: GameStateDao
) {
    fun getGameState(): LiveData<List<GameState>> =
            gameStateDao.getAllGameStates()

    fun createNewGameState(gameState: GameState) =
            gameStateDao.createNewGameState(gameState)

    fun updateGameState(gameState: GameState) =
            gameStateDao.updateGameState(gameState)
}
