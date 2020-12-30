package com.rebllelionandroid.core.database.gamestate

import androidx.annotation.Nullable
import androidx.room.*
import com.rebllelionandroid.core.database.gamestate.enums.Mission
import com.rebllelionandroid.core.database.gamestate.enums.MissionTargetType
import com.rebllelionandroid.core.database.gamestate.enums.UnitType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

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
        @PrimaryKey var id: Long = 0,
        var unitType: UnitType = UnitType.Garrison,
        var team: TeamLoyalty = TeamLoyalty.Neutral,

        @Nullable @ColumnInfo(name = "planet_id", index = true) var locationPlanetId: Long? = 0,
        @Nullable @ColumnInfo(name = "ship_id", index = true) var locationShip: Long? = 0,
        @Nullable var mission: Mission? = Mission.Assassination,
        var dayMissionComplete: Long = 0,
        @Nullable var missionTargetType: MissionTargetType? = MissionTargetType.Factory,
        @Nullable var missionTargetId: Long? = 0,

        @Ignore var updated: Boolean = false,
        @Ignore var created: Boolean = false
)
