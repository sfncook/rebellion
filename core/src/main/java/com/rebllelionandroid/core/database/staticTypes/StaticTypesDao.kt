package com.rebllelionandroid.core.database.staticTypes

import androidx.room.Dao
import androidx.room.Query

@Dao
interface StaticTypesDao {
    @Query("SELECT * FROM sector_types")
    fun getAllSectorTypes(): List<SectorType>

    @Query("SELECT * FROM planet_types WHERE sector_id = :sectorTypeId")
    fun getAllPlanetTypesForSector(sectorTypeId: Long): List<PlanetType>

    @Query("SELECT * FROM unit_types")
    fun getAllUnitTypes(): List<UnitType>
}
