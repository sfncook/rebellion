package com.rebllelionandroid.core.database.gamestate

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import javax.inject.Inject

class GameStateRepository @Inject constructor(
    private val gameStateDao: GameStateDao
) {
    fun getAllGameStates(): LiveData<List<GameState>> =
            gameStateDao.getAllGameStates()

    fun getGameState(): LiveData<GameState> {
        return gameStateDao.getGameState()
    }

    fun createNewGameState(gameState: GameState) =
            gameStateDao.createNewGameState(gameState)

    fun updateGameState(gameState: GameState) =
            gameStateDao.updateGameState(gameState)
}
