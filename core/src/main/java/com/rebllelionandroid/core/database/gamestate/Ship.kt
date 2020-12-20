package com.rebllelionandroid.core.database.gamestate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.gamestate.enums.ShipType
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import org.jetbrains.annotations.NotNull

@Entity(
        tableName = "ships",
        foreignKeys = [ForeignKey(entity = Planet::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("planet_id"),
                onDelete = ForeignKey.CASCADE)])
data class Ship(
        @PrimaryKey val id: Long,
        @ColumnInfo(name = "planet_id", index = true) val locationPlanetId: Long,
        val shipType: ShipType,
        val isTraveling: Boolean,
        val dayArrival: Long,
        val team: TeamLoyalty,
        val attackStrength: Int,
        val defenseStrength: Int,
        val destroyed: Boolean
)
