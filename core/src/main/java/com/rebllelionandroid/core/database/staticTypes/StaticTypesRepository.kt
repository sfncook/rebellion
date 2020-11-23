package com.rebllelionandroid.core.database.staticTypes

import javax.inject.Inject

class StaticTypesRepository @Inject constructor(
    private val staticTypesDao: StaticTypesDao
) {
    fun getAllSectorTypes(): List<SectorType> =
        staticTypesDao.getAllSectorTypes()

    fun getAllPlanetTypesForSector(sectorId: Long): List<PlanetType> =
        staticTypesDao.getAllPlanetTypesForSector(sectorId)

    fun getAllUnitTypes(): List<UnitType> =
        staticTypesDao.getAllUnitTypes()

}
