package com.rebllelionandroid.core.database.staticTypes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sector_types")
data class SectorType(
        @PrimaryKey val id: Long,
        @ColumnInfo(name = "name") val name: String
)
