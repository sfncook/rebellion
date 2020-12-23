package com.rebllelionandroid.core.database

import androidx.room.TypeConverter
import com.rebllelionandroid.core.database.gamestate.enums.*
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.UprisingRank
import java.util.*

class Converters {
    @TypeConverter
    fun toDate(value: Long?) = value?.let { Date(value) }
    @TypeConverter
    fun fromDate(value: Date?) = value?.time

    @TypeConverter
    fun toShipType(value: String?) = value?.let { enumValueOf<ShipType>(it) }
    @TypeConverter
    fun fromShipType(value: ShipType?) = value?.name

    @TypeConverter
    fun toUnitType(value: String?) = value?.let { enumValueOf<UnitType>(it) }
    @TypeConverter
    fun fromUnitType(value: UnitType?) = value?.name

    @TypeConverter
    fun toMission(value: String?) = value?.let { enumValueOf<Mission>(it) }
    @TypeConverter
    fun fromMission(value: Mission?) = value?.name

    @TypeConverter
    fun toMissionTargetType(value: String?) = value?.let { enumValueOf<MissionTargetType>(it) }
    @TypeConverter
    fun fromMissionTargetType(value: MissionTargetType?) = value?.name

    @TypeConverter
    fun toFactoryType(value: String?) = value?.let { enumValueOf<FactoryType>(it) }
    @TypeConverter
    fun fromFactoryType(value: FactoryType?) = value?.name

    @TypeConverter
    fun toFactoryBuildTargetType(value: String?) = value?.let { enumValueOf<FactoryBuildTargetType>(it) }
    @TypeConverter
    fun fromFactoryBuildTargetType(value: FactoryBuildTargetType?) = value?.name

    @TypeConverter
    fun toDefenseStructureType(value: String?) = value?.let { enumValueOf<DefenseStructureType>(it) }
    @TypeConverter
    fun fromDefenseStructureType(value: DefenseStructureType?) = value?.name

    @TypeConverter
    fun toTeamLoyalty(value: String?) = value?.let { enumValueOf<TeamLoyalty>(it) }
    @TypeConverter
    fun fromTeamLoyalty(value: TeamLoyalty?) = value?.name

    @TypeConverter
    fun toUprisingRank(value: String?) = value?.let { enumValueOf<UprisingRank>(it) }
    @TypeConverter
    fun fromUprisingRank(value: UprisingRank?) = value?.name
}
