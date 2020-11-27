package com.rebllelionandroid.core.database.gamestate.enums

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun toShipType(value: String) = enumValueOf<ShipType>(value)

    @TypeConverter
    fun fromShipType(value: ShipType) = value.name
}