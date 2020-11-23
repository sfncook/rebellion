package com.rebllelionandroid.core.database.gamestate

import androidx.lifecycle.LiveData
import javax.inject.Inject
import kotlin.collections.ArrayList

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

    fun getGameStatesWithSectors(): List<GameStateWithSectors> {
        return gameStateDao.getGameStatesWithSectors()
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
        val gameState = gameStateDao.getGameState()
        if(gameState != null) {
            return gameStateDao.getAllSectorsForGameStateId(gameState.id)
        } else {
            return ArrayList()
        }
    }

}
