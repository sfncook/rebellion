package com.rebllelionandroid.core.database.staticTypes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.gamestate.enums.ShipType
import org.jetbrains.annotations.NotNull

@Entity(tableName = "ship_types")
data class StaticShipType(
    @PrimaryKey val id: Long,
    @NotNull @ColumnInfo(name = "ship_type") val shipType: ShipType,
    @NotNull @ColumnInfo(name = "attack_strength") val attackStrength: Long,
    @NotNull @ColumnInfo(name = "defense_strength") val defenseStrength: Long
)
