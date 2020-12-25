package com.rebllelionandroid.core.database.staticTypes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import org.jetbrains.annotations.NotNull

@Entity(tableName = "sector_types")
data class SectorType(
        @PrimaryKey val id: Long,
        @NotNull @ColumnInfo(name = "name") val name: String,
        @NotNull @ColumnInfo(name = "init_team_loyalty") val initTeamLoyalty: TeamLoyalty,
        @NotNull @ColumnInfo(name = "location_index") val locationIndex: Int
)
