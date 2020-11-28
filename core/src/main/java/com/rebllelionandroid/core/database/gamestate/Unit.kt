package com.rebllelionandroid.core.database.gamestate

import androidx.annotation.Nullable
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
                        childColumns = arrayOf("planet_id"),
                        onDelete = ForeignKey.CASCADE),
                ForeignKey(entity = Ship::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("ship_id"),
                        onDelete = ForeignKey.CASCADE)
        ]
)
data class Unit(
        @PrimaryKey val id: Long,
        val unitType: UnitType,
        @Nullable @ColumnInfo(name = "planet_id", index = true) val locationPlanetId: Long?,
        @Nullable @ColumnInfo(name = "ship_id", index = true) val locationShip: Long?,
        @Nullable val mission: Mission?,
        val dayMissionComplete: Long,
        @Nullable val missionTargetType: MissionTargetType?,
        @Nullable val missionTargetId: Long?
)
