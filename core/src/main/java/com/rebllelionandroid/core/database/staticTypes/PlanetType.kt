package com.rebllelionandroid.core.database.staticTypes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(
        tableName = "planet_types",
        foreignKeys = [ForeignKey(entity = SectorType::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("sector_id"))])
data class PlanetType(
        @PrimaryKey val id: Long,
        @NotNull @ColumnInfo(name = "name") val name: String,
        @NotNull @ColumnInfo(name = "sector_id") val sectorId: Long
)
