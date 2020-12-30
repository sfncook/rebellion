package com.rebllelionandroid.core.database.gamestate

import androidx.room.*
import com.rebllelionandroid.core.database.gamestate.enums.ShipType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

@Entity(
        tableName = "ships",
        foreignKeys = [ForeignKey(entity = Planet::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("planet_id"),
                onDelete = ForeignKey.CASCADE)])
data class Ship(
        @PrimaryKey var id: Long = 0,
        var shipType: ShipType = ShipType.Bireme,
        var team: TeamLoyalty = TeamLoyalty.Neutral,
        var attackStrength: Int = 0,
        var defenseStrength: Int = 0,
        var unitCapacity: Int = 0,

        @ColumnInfo(name = "planet_id", index = true) var locationPlanetId: Long = 0,
        var isTraveling: Boolean = false,
        var dayArrival: Long? = null,
        var destroyed: Boolean = false,
        var healthPoints: Int = 0,

        @Ignore var updated: Boolean = false,
        @Ignore var created: Boolean = false
)
