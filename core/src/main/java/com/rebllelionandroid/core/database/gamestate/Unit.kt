package com.rebllelionandroid.core.database.gamestate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.gamestate.enums.Mission
import com.rebllelionandroid.core.database.gamestate.enums.MissionTargetType
import com.rebllelionandroid.core.database.gamestate.enums.UnitType

@Entity(
        tableName = "units",
        foreignKeys = [
                ForeignKey(entity = Planet::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("locationPlanet"),
                        onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = Ship::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("locationShip"),
                        onDelete = ForeignKey.CASCADE)
        ]
)
data class Unit(
        @PrimaryKey val id: Long,
        val unitType: UnitType,
        val locationPlanet: Long,
        val locationShip: Long,
        val mission: Mission,
        val dayMissionComplete: Long,
        val missionTargetType: MissionTargetType,
        val missionTargetId: Long
)
