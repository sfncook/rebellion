package com.rebllelionandroid.core.database.gamestate

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.gamestate.enums.DefenseStructureType
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
data class DefenseStructure(
        @PrimaryKey val id: Long,
        val defenseStructureType: DefenseStructureType,
        val locationPlanet: Planet,
        val isTravelling: Boolean, // Travelling for delivery
        val dayArrival: Long
)
