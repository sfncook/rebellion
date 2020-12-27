package com.rebllelionandroid.core.database.gamestate

import androidx.room.*

@Entity(
        tableName = "planets",
        foreignKeys = [ForeignKey(entity = Sector::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("sector_id"),
                onDelete = ForeignKey.CASCADE)])
data class Planet(
    @PrimaryKey var id: Long = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "sector_id", index = true) var sectorId: Long = 0,
    var locationIndex: Int = 0,
    var teamALoyalty: Int = 0,
    var teamBLoyalty: Int = 0,

    var isExplored: Boolean = false,
    var energyCap: Int = 0,
    var inConflict: Boolean = false,

    @Ignore var updated: Boolean = false
)
