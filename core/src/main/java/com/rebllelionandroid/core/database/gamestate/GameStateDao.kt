package com.rebllelionandroid.core.database.gamestate

import androidx.room.*
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType

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

    @Query("SELECT * FROM units WHERE id = :unitId")
    fun getUnit(unitId: Long): Unit

    @Transaction
    @Query("SELECT * FROM ships WHERE id = :shipId")
    fun getShip(shipId: Long): Ship

    @Transaction
    @Query("SELECT * FROM ships WHERE id = :shipId")
    fun getShipWithUnits(shipId: Long): ShipWithUnits

    @Query("SELECT * FROM units WHERE planet_id = :planetId")
    fun getAllUnitsOnTheSurfaceOfPlanet(planetId: Long): List<Unit>

    @Query("SELECT * FROM game_state WHERE id = :gameStateId")
    fun getGameState(gameStateId: Long): GameState

    @Query("SELECT * FROM sectors WHERE game_state_id = :gameStateId")
    fun getAllSectorsForGameStateId(gameStateId: Long): List<Sector>

    @Query("SELECT * FROM planets")
    fun getAllPlanets(): List<Planet>

    @Query("SELECT * FROM factories WHERE id = :factoryId")
    fun getFactory(factoryId: Long): Factory



    // Inserts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createNewGameState(gameState: GameState)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewSector(sector: Sector)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewPlanet(planet: Planet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewDefenseStructure(defenseStructure: DefenseStructure)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewFactory(factory: Factory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewShip(ship: Ship)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewUnit(unit: Unit)




    // Updates
    @Update
    fun update(gameState: GameState)

    @Update
    fun update(planet: Planet)

    @Update
    fun update(ship: Ship)

    @Delete
    fun delete(ship: Ship)

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
    fun startShipJourneyToPlanet(shipId: Long, planetId: Long, dayArrival: Int): Int

    @Query("UPDATE factories SET buildTargetType = :buildTargetType, dayBuildComplete = :dayBuildComplete, deliver_built_structure_to_planet_id = :destPlanetId WHERE id = :factoryId")
    fun setFactoryBuildOrder(factoryId: Long, dayBuildComplete: Int, buildTargetType: FactoryBuildTargetType, destPlanetId: Long): Int

}
