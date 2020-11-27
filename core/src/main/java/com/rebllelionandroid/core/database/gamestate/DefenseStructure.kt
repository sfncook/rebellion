package com.rebllelionandroid.core.database.gamestate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.gamestate.enums.DefenseStructureType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType

@Entity(
        tableName = "defense_structures",
        foreignKeys = [
                ForeignKey(entity = Planet::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("planet_id"),
                        onDelete = ForeignKey.CASCADE)
        ]
)
data class DefenseStructure(
        @PrimaryKey val id: Long,
        val defenseStructureType: DefenseStructureType,
        @ColumnInfo(name = "planet_id", index = true) val locationPlanetId: Long,
        val isTravelling: Boolean, // Travelling for delivery
        val dayArrival: Long
)
