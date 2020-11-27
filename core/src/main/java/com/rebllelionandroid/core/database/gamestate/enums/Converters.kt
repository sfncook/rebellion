package com.rebllelionandroid.core.database.gamestate.enums

import androidx.room.TypeConverter

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
}
