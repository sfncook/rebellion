package com.rebllelionandroid.core.database.gamestate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        tableName = "planets",
        foreignKeys = [ForeignKey(entity = Sector::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("sector_id"),
                onDelete = ForeignKey.CASCADE)])
data class Planet(
        @PrimaryKey val id: Long,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "sector_id") val sectorId: Long,
        val teamALoyalty: Int,
        val teamBLoyalty: Int
)
