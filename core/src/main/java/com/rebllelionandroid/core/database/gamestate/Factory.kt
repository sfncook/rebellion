package com.rebllelionandroid.core.database.gamestate

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType

@Entity(
        tableName = "factories",
        foreignKeys = [
                ForeignKey(entity = Planet::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("planet_id"),
                        onDelete = ForeignKey.CASCADE)
        ]
)
data class Factory(
        @PrimaryKey val id: Long,
        val factoryType: FactoryType,
        @ColumnInfo(name = "planet_id", index = true) val locationPlanetId: Long,
        val buildTargetType: FactoryBuildTargetType?,
        val dayBuildComplete: Long,
        val isTravelling: Boolean, // Travelling for delivery
        val dayArrival: Long
)
