package com.rebllelionandroid.core.database.gamestate

import javax.inject.Inject

class GameStateRepository @Inject constructor(
    private val gameStateDao: GameStateDao
) {

    fun getAllGameStates() = gameStateDao.getAllGameStates()
    fun getGameStateWithSectorsLive(gameStateId: Long) = gameStateDao.getGameStateWithSectorsLive(gameStateId)
    fun getGameStateWithSectors(gameStateId: Long) = gameStateDao.getGameStateWithSectors(gameStateId)
    fun getSectorWithPlanets(sectorId: Long) = gameStateDao.getSectorWithPlanets(sectorId)
    fun getPlanetWithUnits(planetId: Long) = gameStateDao.getPlanetWithUnits(planetId)
    fun getAllUnitsOnTheSurfaceOfPlanet(planetId: Long) = gameStateDao.getAllUnitsOnTheSurfaceOfPlanet(planetId)
    fun getGameState(gameStateId: Long) = gameStateDao.getGameState(gameStateId)
    fun stopAllGameStates() = gameStateDao.stopAllGameStates()

    fun getAllPlanets() = gameStateDao.getAllPlanets()


    // Inserts
    fun createNewGameState(gameState: GameState) = gameStateDao.createNewGameState(gameState)
    fun insertNewSector(sector: Sector) = gameStateDao.insertNewSector(sector)
    fun insertNewPlanet(planet: Planet) = gameStateDao.insertNewPlanet(planet)
    fun insertNewDefenseStructure(defenseStructure: DefenseStructure) = gameStateDao.insertNewDefenseStructure(defenseStructure)
    fun insertNewFactory(factory: Factory) = gameStateDao.insertNewFactory(factory)
    fun insertNewShip(ship: Ship) = gameStateDao.insertNewShip(ship)
    fun insertNewUnit(unit: Unit) = gameStateDao.insertNewUnit(unit)


    // Updates
    fun updateGameTime(gameStateId: Long, gameTime: Int) = gameStateDao.updateGameTime(gameStateId, gameTime)
    fun setGameInProgress(gameStateId: Long, gameInProgress: Int) = gameStateDao.setGameInProgress(gameStateId, gameInProgress)
    fun updatePlanetLoyalty(planetId:Long, loyalty: Int): Int {
        return gameStateDao.updatePlanetLoyalty(planetId, loyalty)
    }

    fun moveUnitToShip(unitId: Long, shipId: Long) = gameStateDao.moveUnitToShip(unitId, shipId)
    fun moveUnitToPlanet(unitId: Long, planetId: Long) = gameStateDao.moveUnitToPlanet(unitId, planetId)

}
