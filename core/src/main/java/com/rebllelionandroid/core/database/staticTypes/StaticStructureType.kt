package com.rebllelionandroid.core.database.staticTypes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.gamestate.enums.DefenseStructureType
import org.jetbrains.annotations.NotNull

@Entity(tableName = "structure_types")
data class StaticStructureType(
    @PrimaryKey val id: Long,
    @NotNull @ColumnInfo(name = "structure_type") val structureType: DefenseStructureType,
    @NotNull @ColumnInfo(name = "attack_strength") val attackStrength: Int,
    @NotNull @ColumnInfo(name = "defense_strength") val defenseStrength: Int
)
