package com.rebllelionandroid.core.database.gamestate

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType

@Entity(
        tableName = "units",
        foreignKeys = [
                ForeignKey(entity = Planet::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("locationPlanet"),
                        onDelete = ForeignKey.CASCADE)
        ]
)
data class Factory(
        @PrimaryKey val id: Long,
        val factoryType: FactoryType,
        val locationPlanet: Planet,
        val buildTargetType: FactoryBuildTargetType,
        val dayBuildComplete: Long,
        val isTravelling: Boolean, // Travelling for delivery
        val dayArrival: Long
)
