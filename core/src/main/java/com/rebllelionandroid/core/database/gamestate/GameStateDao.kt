package com.rebllelionandroid.core.database.gamestate

import androidx.annotation.Nullable
import androidx.room.*
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionTargetType
import com.rebllelionandroid.core.database.gamestate.enums.MissionType

/**
 * The data access object for the [GameState] class.
 *
 * @see Dao
 */
@Dao
interface GameStateDao {

    @Query("SELECT * FROM game_state")
    fun getAllGameStates(): List<GameState>

    @Transaction
    @Query("SELECT * FROM game_state WHERE id = :gameStateId")
    fun getGameStateWithSectors(gameStateId: Long): GameStateWithSectors

    @Query("SELECT * FROM sectors WHERE id = :sectorId")
    fun getSector(sectorId: Long): Sector

    @Transaction
    @Query("SELECT * FROM planets WHERE id = :planetId")
    fun getPlanetWithUnits(planetId: Long): PlanetWithUnits

    @Query("SELECT * FROM planets WHERE id = :planetId")
    fun getPlanet(planetId: Long): Planet

    @Query("SELECT * FROM units WHERE id = :personnelId")
    fun getPersonnel(personnelId: Long): Personnel

    @Transaction
    @Query("SELECT * FROM ships WHERE id = :shipId")
    fun getShip(shipId: Long): Ship

    @Transaction
    @Query("SELECT * FROM ships WHERE id = :shipId")
    fun getShipWithUnits(shipId: Long): ShipWithUnits

    @Query("SELECT * FROM units WHERE planet_id = :planetId")
    fun getAllUnitsOnTheSurfaceOfPlanet(planetId: Long): List<Personnel>

    @Query("SELECT * FROM game_state WHERE id = :gameStateId")
    fun getGameState(gameStateId: Long): GameState

    @Query("SELECT * FROM sectors WHERE game_state_id = :gameStateId")
    fun getAllSectorsForGameStateId(gameStateId: Long): List<Sector>

    @Query("SELECT * FROM planets")
    fun getAllPlanets(): List<Planet>

    @Query("SELECT * FROM factories WHERE id = :factoryId")
    fun getFactory(factoryId: Long): Factory

    @Query("SELECT * FROM defense_structures WHERE id = :structureId")
    fun getDefenseStructure(structureId: Long): DefenseStructure



    // Inserts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gameState: GameState)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sector: Sector)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(planet: Planet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(defenseStructure: DefenseStructure)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(factory: Factory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ship: Ship)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(personnel: Personnel)




    // Updates
    @Update
    fun update(gameState: GameState)
    @Update
    fun update(planet: Planet)
    @Update
    fun update(ship: Ship)
    @Update
    fun update(factory: Factory)
    @Update
    fun update(personnel: Personnel)


    @Delete
    fun delete(ship: Ship)
    @Delete
    fun delete(factory: Factory)
    @Delete
    fun delete(personnel: Personnel)
    @Delete
    fun delete(defenseStructure: DefenseStructure)

    @Query("UPDATE game_state SET gameInProgress = :gameInProgress WHERE id = :id")
    fun setGameInProgress(id: Long, gameInProgress: Int): Int

    @Query("UPDATE planets SET teamALoyalty = :teamALoyalty, teamBLoyalty = :teamBLoyalty WHERE id = :planetId")
    fun updatePlanetLoyalty(planetId: Long, teamALoyalty: Int, teamBLoyalty: Int): Int

    @Query("UPDATE game_state SET gameInProgress = 0")
    fun stopAllGameStates(): Int

    @Query("UPDATE units SET planet_id = null, ship_id = :shipId WHERE id = :unitId")
    fun moveUnitToShip(unitId: Long, shipId: Long): Int

    @Query("UPDATE units SET planet_id = :planetId, ship_id = null WHERE id = :unitId")
    fun moveUnitToPlanet(unitId: Long, planetId: Long): Int

    @Query("UPDATE ships SET planet_id = :planetId, dayArrival = :dayArrival, isTraveling = 1 WHERE id = :shipId")
    fun startShipJourneyToPlanet(shipId: Long, planetId: Long, dayArrival: Long): Int

    @Query("UPDATE factories SET buildTargetType = :buildTargetType, dayBuildComplete = :dayBuildComplete, deliver_built_structure_to_planet_id = :destPlanetId WHERE id = :factoryId")
    fun setFactoryBuildOrder(factoryId: Long, dayBuildComplete: Long, buildTargetType: FactoryBuildTargetType, destPlanetId: Long): Int

    @Query("UPDATE units SET missionType=:missionType, missionTargetType=:missionTargetType, missionTargetId=:missionTargetId, dayMissionComplete=:dayMissionComplete WHERE id = :personnelId")
    fun assignOrder(personnelId: Long, missionType: MissionType, missionTargetType: MissionTargetType, missionTargetId: Long, dayMissionComplete: Long): Int

    @Query("UPDATE units SET missionType=null, missionTargetType=null, missionTargetId=null, dayMissionComplete=null WHERE id = :personnelId")
    fun cancelMission(personnelId: Long): Int

}
