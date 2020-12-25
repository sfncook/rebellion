package com.rebllelionandroid.core.database.gamestate

import androidx.room.*
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType

@Entity(
        tableName = "factories",
        foreignKeys = [
                ForeignKey(entity = Planet::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("planet_id"),
                        onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = Planet::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("deliver_built_structure_to_planet_id"),
                        onDelete = ForeignKey.CASCADE)
        ]
)
data class Factory(
    @PrimaryKey var id: Long = 0,
    var factoryType: FactoryType = FactoryType.TrainingFaciliy,
    @ColumnInfo(name = "planet_id", index = true) var locationPlanetId: Long = 0,

    var buildTargetType: FactoryBuildTargetType? = FactoryBuildTargetType.ConstructionYard_ConstructionYard,
    var dayBuildComplete: Long = 0,
    @ColumnInfo(name = "deliver_built_structure_to_planet_id", index = true) var deliverBuiltStructureToPlanetId: Long? = null,

    // Once the structure is built then it may need to travel to the 'locationPlanetId' above
    var isTravelling: Boolean = false,
    var dayArrival: Long = 0,

    @Ignore var updated: Boolean = false
)
