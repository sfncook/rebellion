package com.rebllelionandroid.core.database.gamestate

import androidx.annotation.Nullable
import androidx.room.*
import com.rebllelionandroid.core.database.gamestate.enums.MissionType
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
                ForeignKey(entity = Planet::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("return_to_planet_id"),
                        onDelete = ForeignKey.SET_NULL),
                ForeignKey(entity = Ship::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("ship_id"),
                        onDelete = ForeignKey.SET_NULL)
        ]
)
data class Personnel(
        @PrimaryKey var id: Long = 0,
        var unitType: UnitType = UnitType.Garrison,
        var team: TeamLoyalty = TeamLoyalty.Neutral,

        @Nullable @ColumnInfo(name = "planet_id", index = true) var locationPlanetId: Long? = null,
        @Nullable @ColumnInfo(name = "ship_id", index = true) var locationShip: Long? = null,

        @Nullable var missionType: MissionType? = null,
        var dayMissionComplete: Long? = null,
        @Nullable var missionTargetType: MissionTargetType? = null,
        @Nullable var missionTargetId: Long? = null,
        @Nullable @ColumnInfo(name = "return_to_planet_id", index = true) var returnToPlanetId: Long? = null,

        var attackStrength: Int = 0,
        var defenseStrength: Int = 0,
        var destroyed: Boolean = false,
        var maxHealthPoints: Int = 0,
        var healthPoints: Int = 0,

        var isTraveling: Boolean = false,
        var dayArrival: Long = 0,

        @Ignore var updated: Boolean = false,
        @Ignore var created: Boolean = false
)
