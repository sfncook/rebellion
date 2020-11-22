package com.rebllelionandroid.core.database.gamestate

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GameStateRepository @Inject constructor(
    private val gameStateDao: GameStateDao
) {
    fun getAllGameStatesLive(): LiveData<List<GameState>> =
            gameStateDao.getAllGameStatesLive()

    fun getAllGameStates(): List<GameState> =
            gameStateDao.getAllGameStates()

    fun getGameStateLive(): LiveData<GameState> {
        return gameStateDao.getGameStateLive()
    }

    fun getGameState(): GameState {
        return gameStateDao.getGameState()
    }

    fun createNewGameState(gameState: GameState) =
            gameStateDao.createNewGameState(gameState)

    fun updateGameState(gameState: GameState) =
            gameStateDao.updateGameState(gameState)



    fun insertNewSector(sector: Sector) =
            gameStateDao.insertNewSector(sector)

    fun insertNewPlanet(planet: Planet) =
            gameStateDao.insertNewPlanet(planet)

    fun insertNewUnit(unit: Unit) =
            gameStateDao.insertNewUnit(unit)



    fun getAllSectorsForCurrentGame(): List<Sector> {
        val gameStateId = gameStateDao.getGameState().id
        return gameStateDao.getAllSectorsForGameStateId(gameStateId)
    }
}
