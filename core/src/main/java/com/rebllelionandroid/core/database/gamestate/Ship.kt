package com.rebllelionandroid.core.database.gamestate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.gamestate.enums.ShipType

@Entity(
        tableName = "units",
        foreignKeys = [ForeignKey(entity = Planet::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("planet_id"),
                onDelete = ForeignKey.CASCADE)])
data class Ship(
        @PrimaryKey val id: Long,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "planet_id") val location: Long,
        val shipType: ShipType,
        val isTraveling: Boolean,
        val dayArrival: Long
)
