package com.rebllelionandroid.core.database.gamestate

import androidx.lifecycle.LiveData
import androidx.room.*

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

    @Transaction
    @Query("SELECT * FROM game_state WHERE id = :gameStateId")
    fun getGameStateWithSectorsLive(gameStateId: Long): LiveData<GameStateWithSectors>

    @Transaction
    @Query("SELECT * FROM sectors WHERE id = :sectorId")
    fun getSectorWithPlanets(sectorId: Long): SectorWithPlanets

    @Transaction
    @Query("SELECT * FROM planets WHERE id = :planetId")
    fun getPlanetWithUnits(planetId: Long): PlanetWithUnits

    @Query("SELECT * FROM units WHERE planet_id = :planetId")
    fun getAllUnitsOnTheSurfaceOfPlanet(planetId: Long): List<Unit>

    @Query("SELECT * FROM game_state WHERE id = :gameStateId")
    fun getGameState(gameStateId: Long): GameState

    @Query("SELECT * FROM sectors WHERE game_state_id = :gameStateId")
    fun getAllSectorsForGameStateId(gameStateId: Long): List<Sector>


    @Query("SELECT * FROM planets")
    fun getAllPlanets(): List<Planet>



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
    @Query("UPDATE game_state SET gameTime = :gameTime WHERE id = :id")
    fun updateGameTime(id: Long, gameTime: Int): Int

    @Query("UPDATE game_state SET gameInProgress = :gameInProgress WHERE id = :id")
    fun setGameInProgress(id: Long, gameInProgress: Int): Int

    @Query("UPDATE planets SET teamALoyalty = :loyalty WHERE id = :planetId")
    fun updatePlanetLoyalty(planetId:Long, loyalty: Int): Int

    @Query("UPDATE game_state SET gameInProgress = 0")
    fun stopAllGameStates(): Int

    @Query("UPDATE units SET planet_id = null AND ship_id = :shipId WHERE id = :unitId")
    fun moveUnitToShip(unitId: Long, shipId: Long): Int


}
