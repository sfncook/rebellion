package com.rebllelionandroid.core.database.gamestate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.gamestate.enums.ShipType

@Entity(
        tableName = "ships",
        foreignKeys = [ForeignKey(entity = Planet::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("planet_id"),
                onDelete = ForeignKey.CASCADE)])
data class Ship(
        @PrimaryKey val id: Long,
        val name: String,
        @ColumnInfo(name = "planet_id", index = true) val locationPlanetId: Long,
        val shipType: ShipType,
        val isTraveling: Boolean,
        val dayArrival: Long
)
