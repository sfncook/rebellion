package com.rebllelionandroid.core.database.gamestate

import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionType
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
    fun getPersonnel(personnelId: Long) = gameStateDao.getPersonnel(personnelId)
    fun getAllPlanets() = gameStateDao.getAllPlanets()
    fun getFactory(factoryId: Long) = gameStateDao.getFactory(factoryId)


    // Inserts
    fun insert(gameState: GameState) = gameStateDao.insert(gameState)
    fun insert(sector: Sector) = gameStateDao.insert(sector)
    fun insert(planet: Planet) = gameStateDao.insert(planet)
    fun insert(defenseStructure: DefenseStructure) = gameStateDao.insert(defenseStructure)
    fun insert(factory: Factory) = gameStateDao.insert(factory)
    fun insert(ship: Ship) = gameStateDao.insert(ship)
    fun insert(personnel: Personnel) = gameStateDao.insert(personnel)


    // Updates
    fun update(gameState: GameState) = gameStateDao.update(gameState)
    fun update(planet: Planet) = gameStateDao.update(planet)
    fun update(ship: Ship) = gameStateDao.update(ship)
    fun update(factory: Factory) = gameStateDao.update(factory)
    fun delete(ship: Ship) = gameStateDao.delete(ship)

    // Game Play Updates
    fun stopAllGameStates() = gameStateDao.stopAllGameStates()
    fun setGameInProgress(gameStateId: Long, gameInProgress: Int) = gameStateDao.setGameInProgress(gameStateId, gameInProgress)
    fun moveUnitToShip(unitId: Long, shipId: Long) = gameStateDao.moveUnitToShip(unitId, shipId)
    fun moveUnitToPlanet(unitId: Long, planetId: Long) = gameStateDao.moveUnitToPlanet(unitId, planetId)
    fun updatePlanetLoyalty(planetId: Long, teamALoyalty: Int, teamBLoyalty: Int) = gameStateDao.updatePlanetLoyalty(planetId, teamALoyalty, teamBLoyalty)
    fun startShipJourneyToPlanet(shipId: Long, planetId: Long, dayArrival: Long) = gameStateDao.startShipJourneyToPlanet(shipId, planetId, dayArrival)
    fun setFactoryBuildOrder(factoryId: Long, dayBuildComplete: Long, buildTargetType: FactoryBuildTargetType, destPlanetId: Long)
        = gameStateDao.setFactoryBuildOrder(factoryId, dayBuildComplete, buildTargetType, destPlanetId)
    fun assignMission(personnelId: Long, missionType: MissionType, missionTargetType: MissionTargetType, missionTargetId: Long, dayMissionComplete: Long)
        = gameStateDao.assignOrder(personnelId, missionType, missionTargetType, missionTargetId, dayMissionComplete)

}
