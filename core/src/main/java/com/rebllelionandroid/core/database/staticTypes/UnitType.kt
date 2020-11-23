package com.rebllelionandroid.core.database.staticTypes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "unit_types")
data class UnitType(
        @PrimaryKey val id: Long,
        @NotNull @ColumnInfo(name = "name") val name: String,
        @NotNull @ColumnInfo(name = "is_ship") val isShip: Int,
        @NotNull @ColumnInfo(name = "is_personelle") val isPersonelle: Int,
        @NotNull @ColumnInfo(name = "ship_capacity") val shipCapacity: Int,
        @NotNull val manyInitPerTeam: Int
)
