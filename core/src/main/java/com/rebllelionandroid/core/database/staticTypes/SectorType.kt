package com.rebllelionandroid.core.database.staticTypes

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "sector_types")
data class SectorType(
        @PrimaryKey val id: Long,

        @NotNull @ColumnInfo(name = "name") val name: String
)
