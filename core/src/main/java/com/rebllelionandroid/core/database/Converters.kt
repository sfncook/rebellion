package com.rebllelionandroid.core.database

import androidx.room.TypeConverter
import com.rebllelionandroid.core.database.gamestate.enums.*
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class Converters {
    @TypeConverter
    fun toShipType(value: String) = enumValueOf<ShipType>(value)
    @TypeConverter
    fun fromShipType(value: ShipType) = value.name

    @TypeConverter
    fun toUnitType(value: String) = enumValueOf<UnitType>(value)
    @TypeConverter
    fun fromUnitType(value: UnitType) = value.name

    @TypeConverter
    fun toMission(value: String) = enumValueOf<Mission>(value)
    @TypeConverter
    fun fromMission(value: Mission) = value.name

    @TypeConverter
    fun toMissionTargetType(value: String) = enumValueOf<MissionTargetType>(value)
    @TypeConverter
    fun fromMissionTargetType(value: MissionTargetType) = value.name

    @TypeConverter
    fun toFactoryType(value: String) = enumValueOf<FactoryType>(value)
    @TypeConverter
    fun fromFactoryType(value: FactoryType) = value.name

    @TypeConverter
    fun toFactoryBuildTargetType(value: String) = enumValueOf<FactoryBuildTargetType>(value)
    @TypeConverter
    fun fromFactoryBuildTargetType(value: FactoryBuildTargetType) = value.name

    @TypeConverter
    fun toDefenseStructureType(value: String) = enumValueOf<DefenseStructureType>(value)
    @TypeConverter
    fun fromDefenseStructureType(value: DefenseStructureType) = value.name

    @TypeConverter
    fun toTeamLoyalty(value: String) = enumValueOf<TeamLoyalty>(value)
    @TypeConverter
    fun fromTeamLoyalty(value: TeamLoyalty) = value.name
}
