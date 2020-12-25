package com.rebllelionandroid.core.database.gamestate

import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import javax.inject.Inject

class GameStateRepository @Inject constructor(
    private val gameStateDao: GameStateDao
) {

    fun getAllGameStates() = gameStateDao.getAllGameStates()
    fun getGameState(gameStateId: Long) = gameStateDao.getGameState(gameStateId)
    fun getGameStateWithSectors(gameStateId: Long) = gameStateDao.getGameStateWithSectors(gameStateId)
    fun getSector(sectorId: Long) = gameStateDao.getSector(sectorId)
    fun getPlanetWithUnits(planetId: Long) = gameStateDao.getPlanetWithUnits(planetId)
    fun getPlanet(planetId: Long) = gameStateDao.getPlanet(planetId)
    fun getShip(shipId: Long) = gameStateDao.getShip(shipId)
    fun getShipWithUnits(shipId: Long) = gameStateDao.getShipWithUnits(shipId)
    fun getAllUnitsOnTheSurfaceOfPlanet(planetId: Long) = gameStateDao.getAllUnitsOnTheSurfaceOfPlanet(planetId)
    fun getUnit(unitId: Long) = gameStateDao.getUnit(unitId)
    fun getAllPlanets() = gameStateDao.getAllPlanets()
    fun getFactory(factoryId: Long) = gameStateDao.getFactory(factoryId)


    // Inserts
    fun createNewGameState(gameState: GameState) = gameStateDao.createNewGameState(gameState)
    fun insertNewSector(sector: Sector) = gameStateDao.insertNewSector(sector)
    fun insertNewPlanet(planet: Planet) = gameStateDao.insertNewPlanet(planet)
    fun insertNewDefenseStructure(defenseStructure: DefenseStructure) = gameStateDao.insertNewDefenseStructure(defenseStructure)
    fun insertNewFactory(factory: Factory) = gameStateDao.insertNewFactory(factory)
    fun insertNewShip(ship: Ship) = gameStateDao.insertNewShip(ship)
    fun insertNewUnit(unit: Unit) = gameStateDao.insertNewUnit(unit)


    // Updates
    fun stopAllGameStates() = gameStateDao.stopAllGameStates()
    fun setGameInProgress(gameStateId: Long, gameInProgress: Int) = gameStateDao.setGameInProgress(gameStateId, gameInProgress)
    fun moveUnitToShip(unitId: Long, shipId: Long) = gameStateDao.moveUnitToShip(unitId, shipId)
    fun moveUnitToPlanet(unitId: Long, planetId: Long) = gameStateDao.moveUnitToPlanet(unitId, planetId)
    fun updatePlanetLoyalty(planetId: Long, teamALoyalty: Int, teamBLoyalty: Int) = gameStateDao.updatePlanetLoyalty(planetId, teamALoyalty, teamBLoyalty)
    fun startShipJourneyToPlanet(shipId: Long, planetId: Long, dayArrival: Int) = gameStateDao.startShipJourneyToPlanet(shipId, planetId, dayArrival)
    fun setFactoryBuildOrder(factoryId: Long, dayBuildComplete: Int, buildTargetType: FactoryBuildTargetType, destPlanetId: Long)
        = gameStateDao.setFactoryBuildOrder(factoryId, dayBuildComplete, buildTargetType, destPlanetId)
    fun update(gameState: GameState) = gameStateDao.update(gameState)
    fun update(planet: Planet) = gameStateDao.update(planet)
    fun update(ship: Ship) = gameStateDao.update(ship)
    fun delete(ship: Ship) = gameStateDao.delete(ship)

}
