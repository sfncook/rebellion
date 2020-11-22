package com.rebllelionandroid.core.database.staticTypes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        tableName = "planet_types",
        foreignKeys = [ForeignKey(entity = SectorType::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("sector_id"),
                onDelete = ForeignKey.CASCADE)])
data class PlanetType(
        @PrimaryKey val id: Long,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "sector_id") val sectorId: Long
)
