package com.rebllelionandroid.core.database.staticTypes

import javax.inject.Inject

class StaticTypesRepository @Inject constructor(
    private val staticTypesDao: StaticTypesDao
) {
    fun getAllSectorTypes(): List<SectorType> = staticTypesDao.getAllSectorTypes()
    fun getAllPlanetTypesForSector(sectorTypeId: Long): List<PlanetType> =
        staticTypesDao.getAllPlanetTypesForSector(sectorTypeId)
    fun getAllShipTypes(): List<StaticShipType> = staticTypesDao.getAllShipTypes()
    fun getAllStructureTypes(): List<StaticStructureType> = staticTypesDao.getAllStructureTypes()
}
