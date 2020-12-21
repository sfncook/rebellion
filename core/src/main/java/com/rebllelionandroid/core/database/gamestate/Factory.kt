package com.rebllelionandroid.core.database.gamestate

import androidx.annotation.Nullable
import androidx.room.*
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType

@Entity(
        tableName = "factories",
        foreignKeys = [
                ForeignKey(entity = Planet::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("planet_id"),
                        onDelete = ForeignKey.CASCADE)
        ]
)
data class Factory(
        @PrimaryKey var id: Long = 0,
        var factoryType: FactoryType = FactoryType.TrainingFaciliy,
        @ColumnInfo(name = "planet_id", index = true) var locationPlanetId: Long = 0,

        var buildTargetType: FactoryBuildTargetType? = FactoryBuildTargetType.ConstructionYard,
        var dayBuildComplete: Long = 0,
        var isTravelling: Boolean = false, // Travelling for delivery
        var dayArrival: Long = 0,

        @Ignore var updated: Boolean = false
)
