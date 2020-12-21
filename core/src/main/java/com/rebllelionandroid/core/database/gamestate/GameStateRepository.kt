package com.rebllelionandroid.core.database.gamestate

import javax.inject.Inject

class GameStateRepository @Inject constructor(
    private val gameStateDao: GameStateDao
) {

    fun getAllGameStates() = gameStateDao.getAllGameStates()
    fun getGameStateWithSectors(gameStateId: Long) = gameStateDao.getGameStateWithSectors(gameStateId)
    fun getSectorWithPlanets(sectorId: Long) = gameStateDao.getSectorWithPlanets(sectorId)
    fun getPlanetWithUnits(planetId: Long) = gameStateDao.getPlanetWithUnits(planetId)
    fun getShipWithUnits(shipId: Long) = gameStateDao.getShipWithUnits(shipId)
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
    fun setGameInProgress(gameStateId: Long, gameInProgress: Int) = gameStateDao.setGameInProgress(gameStateId, gameInProgress)
    fun moveUnitToShip(unitId: Long, shipId: Long) = gameStateDao.moveUnitToShip(unitId, shipId)
    fun moveUnitToPlanet(unitId: Long, planetId: Long) = gameStateDao.moveUnitToPlanet(unitId, planetId)
    fun startShipJourneyToPlanet(shipId: Long, planetId: Long, dayArrival: Int) = gameStateDao.startShipJourneyToPlanet(shipId, planetId, dayArrival)
    fun update(gameState: GameState) = gameStateDao.update(gameState)
    fun update(planet: Planet) = gameStateDao.update(planet)
    fun update(ship: Ship) = gameStateDao.update(ship)
    fun delete(ship: Ship) = gameStateDao.delete(ship)

}
