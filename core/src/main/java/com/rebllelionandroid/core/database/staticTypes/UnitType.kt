package com.rebllelionandroid.core.database.staticTypes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "unit_types")
data class UnitType(
        @PrimaryKey val id: Long,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "is_ship") val isShip: Int,
        @ColumnInfo(name = "is_personelle") val isPersonelle: Int,
        @ColumnInfo(name = "ship_capacity") val shipCapacity: Int
)
